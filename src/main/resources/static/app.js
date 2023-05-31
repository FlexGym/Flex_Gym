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
        newItem.textContent = `${message.sender.username} : ${message.content}`;

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
            getChatMessages();
        });
    });
}

document.addEventListener("DOMContentLoaded", function() {
    ChatMessageUl = document.querySelector('.chat__message-ul');
    getChatMessages();
    connect();
});