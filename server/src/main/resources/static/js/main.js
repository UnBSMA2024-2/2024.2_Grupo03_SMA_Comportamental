import { httpGet } from './services/HttpService.js';
import SockJsService from './services/SockJsService.js';

const sockJsService = new SockJsService('/truck-updates');
sockJsService.connect();

function publishMessage() {
    sockJsService.publishMessage();
}

window.publishMessage = publishMessage;