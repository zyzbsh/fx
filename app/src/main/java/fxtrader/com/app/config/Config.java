package fxtrader.com.app.config;

public class Config {

	public final static String ProductPriceService = "com.miniwallstreet.service.ProductPriceService";
	public final static String ProductPriceList = "ProductPriceList";

	public final static String CountdownService = "com.miniwallstreet.service.CountdownServiceService";
	public final static String CountdownValue = "CountdownValue";
	public final static String CountdownJson = "CountdownJson";

	public final static String ProductChangeService = "ProductChangeService";
	public final static String ProductCodeValue = "ProductCodeValue";
	public final static String ProductNameValue = "ProductNameValue";
	public final static String ProductStatusValue = "ProductStatusValue";

	// 市场没有开市（默认状态）
	public final static int MARKET_STATUS_NOTOPEN = 0;
	// 开市状态
	public final static int MARKET_STATUS_OPENED = 1;
	// 休息（市场中间休息）
	public final static int MARKET_STATUS_INREST = 2;
	// 闭市（开始收市）
	public final static int MARKET_STATUS_CLOSESTART = 3;
	// 闭市（收盘中，这个中间状态，可能不保存在数据库中，是quant中的标志）
	public final static int MARKET_STATUS_CLOSING = 4;
	// 闭市（收盘完成，做完收盘动作后，设置为closed）
	public final static int MARKET_STATUS_CLOSED = 5;
	// 不开市，可能是节假日休市，也可能是周末休市
	public final static int MARKET_STATUS_NOTWORK = -1;

	// 王庆
	// public static final String BASE_WS_URL = "http://192.168.1.200:9000";
	// public static final String BASE_WS_URL_2 = "http://192.168.1.200:9001";
	// public static final String BASE_WS_URL_3 = "http://192.168.1.200:8080";
	// public static final String BASE_WS_URL_4 = "http://192.168.1.200:8080";
	// public static final String BA_WS_URL_WEB = "http://192.168.1.250:9001";
	// 世荣
	// public static final String BASE_WS_URL = "http://192.168.1.250:9000";

	// 测试服务器
	// public static final String BASE_WS_URL = "http://58.63.244.199:9874";
	// public static final String BASE_WS_URL_2 = "http://58.63.244.199:9877";
	// public static final String BASE_WS_URL_3 = "http://58.63.244.199:13001";
	// public static final String BASE_WS_URL_4 = "http://58.63.244.199:13001";
	// 生产环境
	public static final String BASE_WS_URL = "http://113.105.65.85:9874";
	public static final String BASE_WS_URL_2 = "http://113.105.65.85:9877";
	public static final String BASE_WS_URL_3 = "http://113.105.65.85:13001";
	public static final String BASE_WS_URL_4 = "http://www.igetprofit.com:13001";
	public static final String BA_WS_URL_WEB = "http://www.igetprofit.com";

	private static final String USER = "/api/user/";

	public static final String GETCAPTCHA = BASE_WS_URL + "/api/getCaptcha2/";

	private static final String Order = "/api/trade_order/";

	private static final String Kliner = "/api/kliner/";

	public static final String REGISTER = BASE_WS_URL + USER + "register";

	public static final String LOGIN = BASE_WS_URL + USER + "login";

	public static final String GetOptionList = BASE_WS_URL
			+ "/api/trade_order/getOptionList";

	public static final String Pay = BASE_WS_URL + "/api/bestPay";

	public static final String UpdatePassword = BASE_WS_URL + USER
			+ "updatePwd";

	public static final String UpdateInfo = BASE_WS_URL + USER
			+ "updateUserInfo";

	public static final String GetVarietyList = BASE_WS_URL + Order
			+ "getVarietyList";

	// public static final String GetBalance = BASE_WS_URL + USER +
	// "getBalance";

	public static final String GetBalance = BASE_WS_URL + USER + "getBalance2";

	public static final String SendOption = BASE_WS_URL + Order + "option";

	public static final String Logout = BASE_WS_URL + USER + "logout";

	public static final String GetRechargeRecordList = BASE_WS_URL + Order
			+ "getRechargeRecordList";

	public static final String GetTimeLine = BASE_WS_URL_4
			+ "/api3/kliner/getMinTimeQuotes.do";

	public static final String GetKLine = BASE_WS_URL_4
			+ "/api3/kliner/getKlinerData.do";

	public static final String GetProfitRateAndPrice = BASE_WS_URL + Order
			+ "getProfitRateAndPrice";

	public static final String GetOptionTime = BASE_WS_URL + Order
			+ "getOptionTimes";

	public static final String SellOption = BASE_WS_URL + Order + "sellOption";

	public static final String GetQuoteRate = BASE_WS_URL + Order
			+ "getQuoteRate";

	public static final String GetMarketStatus = BASE_WS_URL + Kliner
			+ "getMarketStatusByCode";

	public static final String GetPushMessage = BASE_WS_URL
			+ "/api/user/getPushMessageList";

	public static final String GetOptionById = BASE_WS_URL
			+ "/api/trade_order/getOptionById";

	public static final String GetCaptcha4pwd = BASE_WS_URL
			+ "/api/getCaptcha4pwd";

	public static final String FindPwd = BASE_WS_URL + "/api/user/findPwd";

	public static final String Protocol = BASE_WS_URL
			+ "/assets/comment/protocol.html";

	public static final String GetProductItemList = BASE_WS_URL
			+ "/api/product/getProductItemlist"; // 获取商城产品列表

	public static final String GetProductById = BASE_WS_URL
			+ "/api/product/getProductById";

	public static final String CheckBindCellPhone = BASE_WS_URL
			+ "/api/user/checkBindCellphone"; // 检查是否已绑定手机号

	public static final String BindCellPhone = BASE_WS_URL
			+ "/api/user/bindCellphone";

	/*
	 * GET /api/exchange 兑换接口 参数pid:Integer, dataJson: String {"productId":1"}
	 */
	public static final String Exchange = BASE_WS_URL + "/api/exchange";

	/*
	 * GET /api/user/getExchangeList page:Integer, size:Integer 获取兑换记录
	 */
	public static final String GetExchangeList = BASE_WS_URL
			+ "/api/user/getExchangeList";

	/*
	 * POST /api/trade_order/doDouble_Delay
	 */
	public static final String DoDoubleDelay = BASE_WS_URL
			+ "/api/trade_order/doDouble_Delay";

	public static final String GetBulletinList = BASE_WS_URL
			+ "/api/bulletin/getBulletinList";
	/*
	 * GET /api/trade_order/getTimelist code:String根据产品类型获取时间
	 */
	public static final String GetTimeList = BASE_WS_URL
			+ "/api/trade_order/getTimelist";

	/*
	 * GET /api/user/getUserOperationList page:Integer, size:Integer
	 */
	public static final String GetUserOperationList = BASE_WS_URL
			+ "/api/user/getUserOperationList";

	public static final String CreateRoom = BASE_WS_URL_2
			+ "/api2/room/createRoom"; // 创建房间

	public static final String GetRoomList = BASE_WS_URL_2
			+ "/api2/room/getRoomList"; // /api/room/getRoomList
	// 获取房间列表

	public static final String CreateOnLooker = BASE_WS_URL_2
			+ "/api2/room/createOnLooker"; // Get 创建围观人 roomId:Integer 参数

	public static final String deleteOnLooker = BASE_WS_URL_2
			+ "/api2/room/deleteOnLooker"; // GET /api/room/deleteOnLooker 删除围观人
	// 参数roomId:Integer

	public static final String GetRoomById = BASE_WS_URL_2
			+ "/api2/room/getRoomById";// /api/room/getRoomById
	// 根据从roomId获取房间详细信息
	// 参数roomId:Integer

	public static final String GetChatList = BASE_WS_URL_2
			+ "/api2/room/getChatList"; // GET
	// /api/room/getChatList
	// roomId:Integer,
	// commentId:Integer
	// 查询评论

	public static final String GetHistoryChatList = BASE_WS_URL_2
			+ "/api2/room/getHistoryChatList"; // roomId:Integer,
	// commentId:Integer,
	// size:Integer
	public static final String AddComment = BASE_WS_URL_2
			+ "/api2/room/addComment";

	public static final String GetOptionListByUserId = BASE_WS_URL_2
			+ "/api2/room/getOptionListByUserId"; // 获取跟单列表 userId:Integer

	public static final String UpdateRoomTitle = BASE_WS_URL_2 //
			+ "/api2/room/updateRoomTitle"; // roomId:Integer, title:String
	// 修改房间标题 登录才能修改

	public static final String GetRoomUserList = BASE_WS_URL_2
			+ "/api2/room/getRoomUserList"; // 参数roomId:Integer 获取房间跟单用户

	public static final String FollowOption = BASE_WS_URL_3
			+ "/api/user/followOption.do"; // 跟单接口 端口8080
	// 参数：optionId：Integer,money:Double,roomId:Integer
	// 创建价格createPrice

	public static final String SearchRoom = BASE_WS_URL_2
			+ "/api2/room/searchRoom";

	// GET /api2/room/deleteRoomChatByChatId 楼主删帖接口 参数 chatId:Integer
	public static final String DeleteRoomChatByChatId = BASE_WS_URL_2
			+ "/api2/room/deleteRoomChatByChatId";

	// GET /api2/room/getFavoriteRoomList 获取我的收藏列表 参数 page:Integer, size:Integer
	public static final String GetFavoriteRoomList = BASE_WS_URL_2
			+ "/api2/room/getFavoriteRoomList";

	// GET /api2/room/deleteFavoriteRoom 取消收藏 参数 roomId:Integer
	public static final String DeleteFavoriteRoom = BASE_WS_URL_2
			+ "/api2/room/deleteFavoriteRoom";

	// GET /api2/room/checkFavoriteRoom 检查是否已收藏房间
	public static final String CheckFavoriteRoom = BASE_WS_URL_2
			+ "/api2/room/checkFavoriteRoom";

	// GET /api2/room/addFavoriteRoom 收藏房间 参数 roomId:Integer
	public static final String AddFavoriteRoom = BASE_WS_URL_2
			+ "/api2/room/addFavoriteRoom";

	// GET /api2/getDayTopList 获取日排行榜 参数 create_time:String
	public static final String GetDayTopList = BASE_WS_URL_2
			+ "/api2/getDayTopList";

	// GET /api2/getWeekTopList 获取周排行榜 week:Integer
	public static final String GetWeekTopList = BASE_WS_URL_2
			+ "/api2/getWeekTopList";

	// GET /api2/getSystemMsgByType 获取有效公告接口
	// 参数 type:Integer type默认先为1
	// 返回参数 private int id;
	// private String message;//公告内容
	// private String createTime;
	// private int msgType;
	// private String validTime;//有效时间
	// private String title;//公告标题
	public static final String GETSystemMsgByType = BASE_WS_URL
			+ "/api/getSystemMsgByType";

	// /api/getRollMsg type:Integer 获取滑动公告信息 0 - ios 1 - android 2 -h5

	public static final String GetRollMsg = BASE_WS_URL
			+ "/api/getRollMsg?type=1";

	public static final String GetRollMsg2 = BASE_WS_URL_2
			+ "/api2/getRollMsg?type=1";

	public static final String CheckSignTicket = BASE_WS_URL_2
			+ "/api2/checkSignTicket";

	// GET /api2/signTickets 签到送赚票
	// befrom:Integer 参数 0- 来自app 1-来自h5
	public static final String SignTickets = BASE_WS_URL_2
			+ "/api2/signTickets";

	// GET /api2/getTicketListByUserId 改一下 用这个url
	public static final String GetTicketList = BASE_WS_URL
			+ "/api/getTicketListByUserId";

	// GET /api/getTickesByUserId
	// 获取用户的赚票列表
	public static final String GetTickets = BASE_WS_URL
			+ "/api/getTickesByUserId";

	// GET /api/getTicketCountsByUserId
	// 获取用户可用的赚票数量
	// 加头校验
	public static final String GetTicketCount = BASE_WS_URL
			+ "/api/getTicketCountsByUserId";

	// GET /api/other/getClientInfoByVersion 强制更新
	// 参数：version:String
	public static final String GetVersionInfo = BASE_WS_URL
			+ "/api/other/getClientInfoByVersion";

	// GET /api/activity/checkActivityShare
	// 检查宝箱是否已完成
	public static final String CheckActivityShare = BASE_WS_URL
			+ "/api/activity/checkActivityShare";

	public static final String ZPActivityShare = BASE_WS_URL
			+ "/api/activity/zpShare";

	// GET /api/getHistorySystemMsg page:Integer, size:Integer
	// 获取公告信息 端口9000
	public static final String GetHistorySystemMsg = BASE_WS_URL
			+ "/api/getHistorySystemMsg";

	// 大师赛报名
	public static final String MasterSignUp = BASE_WS_URL_2
			+ "/api2/masterSignUp";

	public static final String RankingList = BA_WS_URL_WEB
			+ "/master/com/app_ranking_list.html";

	public static boolean isSaveFlux = false;
}
