import Utils from './Utils'
import Config from './config'
import Permission from './Permission'
import ElementUI from 'element-ui'

export default {
  sysMsgSocket() {
    let token = Permission.getToken();
    if (!token) {
      return null;
    }
    return Utils.socketBuilder(`${Config.sockeUrl}/sysmsg/${token}`)
      .message(event => {
        let message = JSON.parse(event.data);
        let type;
        switch(message.type) {
             case 1:
                type = 'info';
                break;
             case 2:
                type = 'success';
                break;
             case 3:
                type = 'error';
                break;
             default:
                type = 'info';
        } 
        ElementUI.Notification({
          title: '系统消息',
          message: message.data,
          type
        });
      })
      .open(event => console.log(event)).build();
  }
}
