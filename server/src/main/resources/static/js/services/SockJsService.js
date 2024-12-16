import * as StompJs from '../libs/stomp.js';

class SockJsService {

    constructor(endpoint) {
        this._stompClient = new window.StompJs.Client({
            brokerURL: 'ws://localhost:8080' + endpoint,
        });

        this._stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);
            this._stompClient.subscribe('/topic/updates', (message) => {
                console.log(JSON.parse(message.body));
            });
        };

        this._stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);
            this._stompClient.subscribe('/topic/ants/agents/alive', (message) => {
                console.log(JSON.parse(message.body));
            });
        };

        this._stompClient.onWebSocketError = (error) => {
            console.error('Error with websocket', error);
        };

        this._stompClient.onStompError = (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        };
    }

    connect() {
        this._stompClient.activate();
    }

    disconnect() {
        this._stompClient.deactivate();
        console.log("Disconnected");
    }

    publishMessage(message) {
        this._stompClient.publish({
            destination: "/app/hello",
            body: JSON.stringify({'name': 'teste'})
        });
    }

}

export default SockJsService;