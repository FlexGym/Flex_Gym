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

        if (message.sender.user_id === memberId) {
            newItem.classList.add("sender");
        } else {
            newItem.classList.add("receiver");
        }

        if (message.type == "ENTER"){
            newItem.textContent = `${message.content}`;
        } else {
            const createdAt = new Date(message.created_at);

            // 가공된 시간 표시 형식 (MM:SS)
            const hours = String(createdAt.getHours()).padStart(2, '0');
            const minutes = String(createdAt.getMinutes()).padStart(2, '0');
            const formattedTime = `${hours}:${minutes}`;

            newItem.textContent = `${message.sender.username} : ${message.content} <${formattedTime}>`;
        }

        ChatMessageUl.appendChild(newItem);
    });
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
            // showGreeting(JSON.parse(chatMessage.body));
            getChatMessages();
        });
    });
}

document.addEventListener("DOMContentLoaded", function() {
    ChatMessageUl = document.querySelector('.chat__message-ul');
    getChatMessages();
    connect();
});

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
        console.log('Disconnected');
    }
}

function showGreeting(chatMessage) {
    console.log(chatMessage.name)
    $("#chatting").append("<tr><td>" + "[" + chatMessage.name + "]" + chatMessage.message + "</td></tr>");
}