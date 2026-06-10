import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

let stompClient: Client | null = null;

export const connectWS = (onMessage: (msg: any) => void) => {

    stompClient = new Client({
        webSocketFactory: () => new SockJS("http://localhost:8080/ws"),

        reconnectDelay: 5000,

        onConnect: () => {
            console.log("WS connected");

            // subscribe topic
            stompClient?.subscribe("/user/queue/notifications", (message) => {
                onMessage(JSON.parse(message.body));
            });
        },

        onStompError: (frame) => {
            console.error("Broker error:", frame.headers["message"]);
        }
    });

    stompClient.activate();
};


export const sendMessage = (content: string) => {
    stompClient?.publish({
        destination: "/app/chat",
        body: JSON.stringify({
            content
        })
    });
};