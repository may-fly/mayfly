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
        ElementUI.Notification.info({
          title: '系统消息',
          message: event.data
        });
      })
      .open(event => console.log(event)).build();
  }
}
