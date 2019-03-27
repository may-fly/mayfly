export default {
  siteName:'Mayfly Admin',
  minSiteMame:'EUI',
  tokenKey: 'token',//存在sessionStorage里面的tokenKey
	menusKey: 'menus',
	permissionsKey: "permissions",
	
  postStatus:{
      draft:'草稿',
      published:'发布',
      recycled:"垃圾"
  },
  postType:{
      AR:'AR资讯',
      HEALTH:'健康资讯',
  },
  sex:{
      male:'男',
      female:'女',
      unknown:'未知'
  },
	result: {
		success: 200,
		error: 400,
		paramError: 405
	}
}
