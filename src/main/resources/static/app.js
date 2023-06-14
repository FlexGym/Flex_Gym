let stompClient = null;
let fromId = 0;
let ChatMessageUl = null;

function getChatMessages() {
    console.log("fromId : " + fromId);
    fetch(`/usr/chat/rooms/${chatRoomId}/messages?fromId=${fromId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }})
        .then(response => response.json())
        .then(body => {
            drawMessages(body);
        });
}

function drawMessages(messages) {
    if (messages.length > 0) {
        fromId = messages[messages.length - 1].message_id;
    }

    messages.forEach((message) => {

        const newItem = document.createElement("li");
        console.log(message);
        console.log("memberId : " + memberId);
        console.log("memberName : " + memberName);

        if (message.sender.user_id === memberId) {
            newItem.classList.add("sender");
        } else {
            newItem.classList.add("receiver");
        }

        if (message.type == "ENTER"){
            newItem.classList.add("center");
            newItem.textContent = `${message.content}`;
        } else {
            const createdAt = new Date(message.created_at);

            // 가공된 시간 표시 형식 (MM:SS)
            const hours = String(createdAt.getHours()).padStart(2, '0');
            const minutes = String(createdAt.getMinutes()).padStart(2, '0');
            const formattedTime = `${hours}:${minutes}`;

            // newItem.textContent = `${message.sender.username} : ${message.content} <${formattedTime}>`;
            newItem.innerHTML = `${message.sender.username} : ${message.content} <span class="message-time"><${formattedTime}></span>`;
        }

        ChatMessageUl.appendChild(newItem);
    });

    scrollToBottom();
}

function ChatWriteMessage(form) {

    form.content.value = form.content.value.trim();

    stompClient.send(`/app/chats/${chatRoomId}/sendMessage`, {}, JSON.stringify({content: form.content.value}));

    form.content.value = '';
    form.content.focus();
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    const headers = {
        'X-CSRF-TOKEN': token,
    };

    stompClient.connect(headers, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe(`/topic/chats/${chatRoomId}`, function (data) {
            getChatMessages();
        });

        stompClient.subscribe(`/topic/chats/${chatRoomId}/kicked`, function (data) {
            console.log(data);
            if (data.body == memberId) {
                disconnect();
                location.reload();
            }
        });
    });
}

document.addEventListener("DOMContentLoaded", function() {
    ChatMessageUl = document.querySelector('.chat__message-ul');
    getChatMessages();
    // getUserList();
    connect();
});

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
        console.log('Disconnected');
    }
}

// 참가자 목록 가져오기 ( 사용 x, 추후 사용)
function getUserList() {
    fetch(`/usr/chat/rooms/${chatRoomId}/members`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }
    })
        .then(response => response.json())
        .then(members => {
            const userList = document.getElementById("userList");
            userList.innerHTML = ""; // 이전 목록 초기화

            members.forEach(member => {
                const listItem = document.createElement("a");
                listItem.classList.add("dropdown-item");
                listItem.textContent = member.username;
                userList.appendChild(listItem);
            });
        });
}

function scrollToBottom() {
    const chatMessages = document.querySelector('.chat-messages');
    chatMessages.scrollTop = chatMessages.scrollHeight;
}