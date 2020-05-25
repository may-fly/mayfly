import Utils from './Utils'
import Config from './config'
import Permission from './Permission'
import ElementUI from 'element-ui'

export default {
  /**
   * 全局系统消息websocket
   */
  sysMsgSocket() {
    let token = Permission.getToken();
    if (!token) {
      return null;
    }
    return Utils.socketBuilder(`${Config.socketUrl}/sysmsg/${token}`)
      .message((event: { data: string }) => {
        let message = JSON.parse(event.data);
        let type: string;
        switch (message.type) {
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
        if (type == undefined) {
          return;
        }
        ElementUI.Notification({
          title: '系统消息',
          message: message.data
        });
      })
      .open((event: any) => console.log(event)).build();
  }
}
