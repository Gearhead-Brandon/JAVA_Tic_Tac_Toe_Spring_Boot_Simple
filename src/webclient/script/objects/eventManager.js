class EventManager {
    constructor() {
      this.listeners = {};
    }
  
    subscribe(eventName, callback) {
      if (!this.listeners[eventName])
        this.listeners[eventName] = [];
      
      this.listeners[eventName].push(callback);
    }
  
    emit(eventName, data) {
      if (this.listeners[eventName])
        this.listeners[eventName].forEach(callback => callback(data));
    }
}

export const eventManager = new EventManager();

