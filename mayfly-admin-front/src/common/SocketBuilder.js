class SocketBuilder {
  
  constructor(url) {
    if (typeof(WebSocket) === "undefined") {
      throw new Error('不支持websocket');
    }
    if (!url) {
      throw new Error('websocket url不能为空');
    }
    this.websocket = new WebSocket(url);
  }

  static builder(url) {
    return new SocketBuilder(url);
  }
  
  open(onopen) {
    this.websocket.onopen = onopen;
    return this;
  }
  
  error(onerror) {
    this.websocket.onerror = onerror;
    return this;
  }
  
  message(onmessage) {
    this.websocket.onmessage = onmessage;
    return this;
  }
  
  close(onclose) {
    this.websocket.onclose = onclose;
    return this;
  }
  
  build() {
    return this.websocket;
  }
}

export default SocketBuilder;
