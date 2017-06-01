//package com.xk.retrofitdemo.manager;
//
//import android.graphics.Bitmap;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.gntv.tv.common.ap.AuthManager;
//import com.gntv.tv.common.base.BaseInfo;
//import com.gntv.tv.common.utils.DateUtil;
//import com.gntv.tv.common.utils.DeviceUtil;
//import com.gntv.tv.common.utils.LogUtil;
//import com.gntv.tv.common.utils.NetUtil;
//import com.gntv.tv.common.utils.QRCodeUtil;
//import com.gntv.tv.common.vuser.VUserManager;
//import com.google.gson.Gson;
//import com.voole.epg.corelib.model.account.bean.CheckGiftCardInfo;
//import com.voole.epg.corelib.model.account.bean.ConsumeListInfo;
//import com.voole.epg.corelib.model.account.bean.FilmPrice;
//import com.voole.epg.corelib.model.account.bean.GetOrderResult;
//import com.voole.epg.corelib.model.account.bean.GiftCardInfo;
//import com.voole.epg.corelib.model.account.bean.MessageInfoResult;
//import com.voole.epg.corelib.model.account.bean.OrderListInfo;
//import com.voole.epg.corelib.model.account.bean.OrderResult;
//import com.voole.epg.corelib.model.account.bean.PlayCheckInfo;
//import com.voole.epg.corelib.model.account.bean.PlayFilmInfo;
//import com.voole.epg.corelib.model.account.bean.PlayInfo;
//import com.voole.epg.corelib.model.account.bean.PlayInfo.OrderInfo;
//import com.voole.epg.corelib.model.account.bean.PlayTimeInfo;
//import com.voole.epg.corelib.model.account.bean.Product;
//import com.voole.epg.corelib.model.account.bean.ProductListInfo;
//import com.voole.epg.corelib.model.account.bean.ProductsOrderList;
//import com.voole.epg.corelib.model.account.bean.ProductsOrderStateParser;
//import com.voole.epg.corelib.model.account.bean.RechargeListInfo;
//import com.voole.epg.corelib.model.account.bean.RechargeResult;
//import com.voole.epg.corelib.model.account.bean.WeiXinQRItem;
//import com.voole.epg.corelib.model.account.parser.CheckGiftCardInfoParser;
//import com.voole.epg.corelib.model.account.parser.ConsumeListInfoParser;
//import com.voole.epg.corelib.model.account.parser.FilmCollectParse;
//import com.voole.epg.corelib.model.account.parser.FilmPlayParse;
//import com.voole.epg.corelib.model.account.parser.FilmPriceParser;
//import com.voole.epg.corelib.model.account.parser.GetOrderResultParser;
//import com.voole.epg.corelib.model.account.parser.GiftCardInfoParser;
//import com.voole.epg.corelib.model.account.parser.MessageInfoResultParser;
//import com.voole.epg.corelib.model.account.parser.OrderListInfoParser;
//import com.voole.epg.corelib.model.account.parser.OrderResultParser;
//import com.voole.epg.corelib.model.account.parser.PlayCheckInfoNewParser;
//import com.voole.epg.corelib.model.account.parser.PlayInfoParse;
//import com.voole.epg.corelib.model.account.parser.PlayTimeInfoParse;
//import com.voole.epg.corelib.model.account.parser.ProductParse;
//import com.voole.epg.corelib.model.account.parser.RechargeListInfoParser;
//import com.voole.epg.corelib.model.account.parser.RechargeResultParser;
//import com.voole.epg.corelib.model.account.parser.WeiXinQRItemParser;
//import com.voole.epg.corelib.model.movie.bean.Film;
//import com.voole.epg.corelib.model.url.Key;
//import com.voole.epg.corelib.model.url.UrlManager;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Random;
//import java.util.Set;
//import java.util.TreeMap;
//
//public class AccountManager {
//    private static final String ACCOUNT_TERMINAL_TYPE_TV = "2";
//
//    private static final String VERSION = "1.2";
//    private static final boolean IS_USE_AUTH = false;
//    private static final String TAG = AccountManager.class.getSimpleName();
//    private static final boolean DEBUG = true;
//    /**
//     * 普通充值(充值)
//     */
//    private static final String ORDER_TYPE_RECHARGE = "0";
//    /**
//     * 单点充值(充值、消费)
//     */
//    private static final String ORDER_TYPE_FILM = "1";
//    /**
//     * 订购充值(充值、消费、订购)
//     */
//    private static final String ORDER_TYPE_PRODUCT = "2";
//
//    private String appType = "";
//
//
//    private AccountManager() {
//    }
//
//    private static class SingletonHolder {
//        private static final AccountManager instance = new AccountManager();
//    }
//
//    public void init(String appType) {
//        this.appType = appType;
//    }
//
//    /**
//     * for K12
//     *
//     * @param films
//     * @return
//     */
//    public ProductsOrderList getFilmOrderInfoUrl(List<Film> films) {
//        String url = getFilmOrderUrl(films);
//        if (TextUtils.isEmpty(url)) {
//            return null;
//        }
//        try {
//            ProductsOrderStateParser parser = new ProductsOrderStateParser();
//            parser.setUrl(url);
//            return parser.getmProductsOrderList();
//        } catch (Exception e) {
//            LogUtil.d("getFilmOrderInfoUrl----->Exceptione:" + (e == null ? "" : e.toString()));
//        }
//        return null;
//    }
//
//    public static AccountManager getInstance() {
//        return SingletonHolder.instance;
//    }
//
//
//    private String getFilmOrderUrl(List<Film> films) {
//        // for tet
//        // userauth.voole.com   172.16.10.12  海尔的测试环境地址
//        // http://10.5.3.13/v3a_userauth/Agent.do?oemid=817&uid=5621761&hid=5cc6d0cbcd5b0000000000000000000000000000&param={%22action%22:%22eduproduct%22,%22movielist%22:[{%22mid%22:%221241364%22},{%22mid%22:%223362928%22}],%22version%22:%22v1.0%22}
//        String url = "";
//        String parm = getMoviesMids(films).toString();
//        url = getAuthUrl(parm);
//        LogUtil.d("getFilmOrderUrl:" + url);
//        return url;
//    }
//
//    private JSONObject getMoviesMids(List<Film> films) {
//        JSONObject movieList = new JSONObject();
//        if (films == null) {
//            return movieList;
//        }
//        try {
//            movieList = new JSONObject();
//            movieList.put("action", "eduproduct");
//            JSONArray array = new JSONArray();
//            int size = films.size();
//            for (int i = 0; i < size; i++) {
//                JSONObject mid = new JSONObject();
//                mid.put("mid", films.get(i).getMid());
//                array.put(mid);
//            }
//            movieList.put("movielist", array);
//            movieList.put("version", "v1.0");
//            LogUtil.d("getMoviesMids:" + movieList.toString());
//            return movieList;
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 续播-获取某个影片的播放记录
//     *
//     * @param userid   选填 用户系统userid
//     * @param aid      必填 影片专辑ID
//     * @param mstype   选填 节目单集类型
//     * @param page     选填 分页页码，默认1
//     * @param pagesize 选填 每页显示个数，默认12
//     * @param babyid   选填 小朋友id
//     */
//    public PlayFilmInfo getPlayHistory(String userid, String aid, String mstype, int page, int pagesize,
//                                       String babyid) {
//
//        try {
//            String dll = UrlManager.getInstance().getDLL(Key.KEY_RESUME_PLAY);
//            if (dll == null || TextUtils.isEmpty(aid)) {
//                return null;
//            }
//
//            StringBuffer url = new StringBuffer(dll);
//            if (userid != null)
//                url.append("&userid=" + userid);
//            url.append("&aid=" + aid);
//
//            if (!TextUtils.isEmpty(mstype))
//                url.append("&mstype=" + mstype);
//            url.append("&page=" + page);
//            url.append("&pagesize=" + pagesize);
//            url.append("&datetime=" + DateUtil.getDateTime());
//            url.append("&version=" + VERSION);
//
//            if (!TextUtils.isEmpty(babyid))
//                url.append("&babyid=" + babyid);
//            url.append("&format=0");
//            url.append("&ctype=6");
//            url.append("&apptype=" + appType);
//            showLog(url.toString());
//
//            LogUtil.d("getPlayHistory--url->" + url.toString());
//
//            PlayInfoParse playInfoParse = new PlayInfoParse();
//
//            playInfoParse.setUrl(url.toString());
//
//            return playInfoParse.getPlayFilmInfo();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("getPlayHistory--url->Exception:   " + e.getMessage());
//        }
//        return null;
//
//    }
//
//    /**
//     * 续播-获取某个用户的播放记录
//     *
//     * @param userid   选填 用户系统userid
//     * @param aid      选填 影片专辑ID
//     * @param page     选填 分页页码，默认1
//     * @param pagesize 选填 每页显示个数，默认12
//     * @param babyid   选填 小朋友id
//     */
//    public PlayFilmInfo getPlayHistory(String userid, String aid, int page, int pagesize, String babyid) {
//
//        try {
//            String dll = UrlManager.getInstance().getDLL(Key.KEY_RESUME_PLAY);
//            if (dll == null) {
//                return null;
//            }
//
//            StringBuffer url = new StringBuffer(dll);
//            if (userid != null)
//                url.append("&userid=" + userid);
//            if (aid != null)
//                url.append("&aid=" + aid);
//
//            url.append("&page=" + page);
//            url.append("&pagesize=" + pagesize);
//            url.append("&datetime=" + DateUtil.getDateTime());
//            url.append("&version=" + VERSION);
//
//            if (babyid != null)
//                url.append("&babyid=" + babyid);
//            url.append("&format=0");
//            url.append("&ctype=3");
//            url.append("&apptype=" + appType);
//            showLog(url.toString());
//
//            LogUtil.d("getPlayHistory--url->" + url.toString());
//
//            PlayInfoParse playInfoParse = new PlayInfoParse();
//
//            playInfoParse.setUrl(url.toString());
//
//            return playInfoParse.getPlayFilmInfo();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("getPlayHistory--url->Exception:   " + e.getMessage());
//        }
//        return null;
//
//    }
//
//    /**
//     * 续播-获取某个影片的播放时间 选填null
//     *
//     * @param mid    选填 影片mid
//     * @param sid    必须 影片剧集
//     * @param aid    必须 影片专辑ID
//     * @param msid   必须 节目单集id
//     * @param userid 选填 用户系统userid
//     * @param mtype  选填 影片类型[0,电影|1,电视剧]
//     * @param babyid 选填 小朋友id
//     * @return
//     */
//    public PlayTimeInfo getPlayTime(String mid, String sid, String aid, String msid, String userid, String mtype,
//                                    String babyid) {
//        try {
//            String dll = UrlManager.getInstance().getDLL(Key.KEY_RESUME_PLAY);
//            if (TextUtils.isEmpty(dll)) {
//                return null;
//            }
//            StringBuffer url = new StringBuffer(dll);
//            if (mid != null)
//                url.append("&mid=" + mid);
//            if (aid != null)
//                url.append("&aid=" + aid);
//            if (msid != null)
//                url.append("&msid=" + msid);
//            if (userid != null)
//                url.append("&userid=" + userid);
//            if (sid != null)
//                url.append("&sid=" + sid);
//            if (mtype != null)
//                url.append("&mtype=" + mtype);
//            url.append("&datetime=" + DateUtil.getDateTime());
//            url.append("&version=" + VERSION);
//            if (babyid != null)
//                url.append("&babyid=" + babyid);
//            url.append("&format=0");
//            url.append("&ctype=1");
//            url.append("&apptype=" + appType);
//            showLog(url.toString());
//
//            LogUtil.d("getPlayTime--url->" + url.toString());
//
//            PlayTimeInfoParse playTimeInfoParse = new PlayTimeInfoParse();
//            playTimeInfoParse.setUrl(url.toString());
//            return playTimeInfoParse.getPlayTime();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("getPlayTime--url->Exception:    " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 续播-删除某个用户的播放记录
//     *
//     * @param userid 选填 用户系统userid
//     * @param aid    选填 影片专辑ID
//     * @param msid   选填 节目单集id
//     * @param sid    选填 影片剧集
//     * @param babyid 选填 小朋友id
//     * @return
//     */
//    public BaseInfo deletePlayHistory(String userid, String aid, String msid, String sid, String babyid) {
//
//        try {
//            // 动态地址
//            String dll = UrlManager.getInstance().getDLL(Key.KEY_RESUME_PLAY);
//            if (TextUtils.isEmpty(dll)) {
//                return null;
//            }
//            StringBuffer url = new StringBuffer(dll);
//            if (userid != null)
//                url.append("&userid=" + userid);
//            if (aid != null)
//                url.append("&aid=" + aid);
//            if (msid != null)
//                url.append("&msid=" + msid);
//            if (sid != null)
//                url.append("&sid=" + sid);
//            url.append("&datetime=" + DateUtil.getDateTime());
//            url.append("&version=" + VERSION);
//            if (babyid != null)
//                url.append("&babyid=" + babyid);
//
//            url.append("&format=0");
//            url.append("&ctype=4");
//            url.append("&apptype=" + appType);
//            showLog(url.toString());
//            LogUtil.d("deletePlayHistory--url->" + url.toString());
//
//            FilmPlayParse filmPlayParse = new FilmPlayParse();
//            filmPlayParse.setUrl(url.toString());
//            return filmPlayParse.getBaseInfo();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("deletePlayHistory--url->Exception:   " + e.getMessage());
//        }
//        return null;
//
//    }
//
//    /**
//     * 续播-添加或者更新播放记录
//     *
//     * @param mid       必须 影片mid
//     * @param sid       必须 影片剧集
//     * @param userid    选填 用户系统userid
//     * @param aid       必须 影片专辑ID
//     * @param msid      必须 节目单集id
//     * @param mstype    必须 节目单集类型
//     * @param mtype     选填 影片类型[0,电影|1,电视剧]
//     * @param title     必须 影片名称 – urlencode(utf-8)加密后字符串
//     * @param Imgurl    选填 影片海报地址
//     * @param totaltime 选填 影片总时长
//     * @param playtime  必须 播放时间
//     * @param babyid    选填 选填 小朋友id
//     * @return
//     */
//    public BaseInfo UpdatePlayHistoryInfo(String mid, String sid, String userid, String aid, String msid, String mstype,
//                                          String mtype, String title, String Imgurl, String totaltime, String playtime, String babyid) {
//
//        try {
//            // 动态地址
//            String dll = UrlManager.getInstance().getDLL(Key.KEY_RESUME_PLAY);
//            if (TextUtils.isEmpty(dll)) {
//                return null;
//            }
//            StringBuffer url = new StringBuffer(dll);
//            if (mid != null)
//                url.append("&mid=" + mid);
//            if (sid != null)
//                url.append("&sid=" + sid);
//            if (userid != null)
//                url.append("&userid=" + userid);
//            if (aid != null)
//                url.append("&aid=" + aid);
//            if (msid != null)
//                url.append("&msid=" + msid);
//            if (mstype != null)
//                url.append("&mstype=" + mstype);
//            if (mtype != null)
//                url.append("&mtype=" + mtype);
//            if (title != null)
//                url.append("&title=" + URLEncoder.encode(title, "UTF-8"));
//            if (Imgurl != null)
//                url.append("&Imgurl=" + Imgurl);
//            if (totaltime != null)
//                url.append("&totaltime=" + totaltime);
//            if (playtime != null)
//                url.append("&playtime=" + playtime);
//            url.append("&datetime=" + DateUtil.getDateTime());
//            url.append("&version=" + VERSION);
//            if (babyid != null)
//                url.append("&babyid=" + babyid);
//            url.append("&format=0");
//            url.append("&ctype=7");
//            url.append("&apptype=" + appType);
//            showLog(url.toString());
//
//            LogUtil.d("UpdatePlayHistoryInfo--url->" + url.toString());
//
//            FilmPlayParse filmPlayParse = new FilmPlayParse();
//            filmPlayParse.setUrl(url.toString());
//            return filmPlayParse.getBaseInfo();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("UpdatePlayHistoryInfo--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 请求赠卡
//     *
//     * @return
//     */
//    public GiftCardInfo giftCard() {
//        String url = getGiftCardUrl();
//        GiftCardInfoParser parser = new GiftCardInfoParser();
//        try {
//            parser.setUrl(url);
//            return parser.getGiftCardInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("GiftCardInfo--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 赠卡检测
//     *
//     * @return
//     */
//    public CheckGiftCardInfo checkGiftCard() {
//        String url = getCheckGiftCardUrl();
//        CheckGiftCardInfoParser parser = new CheckGiftCardInfoParser();
//        try {
//            parser.setUrl(url);
//            return parser.getCheckGiftCardInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("CheckGiftCardInfo--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    private String getGiftCardUrl() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"giftcard\",");
//        sb.append("\"versioin\":\"1\"}");
//        String url = getAuthUrl(sb.toString());
//        LogUtil.d("getGiftCardUrl--------->" + url);
//        return url;
//    }
//
//    private String getCheckGiftCardUrl() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"checkgiftcard\",");
//        sb.append("\"versioin\":\"1\"}");
//        String url = getAuthUrl(sb.toString());
//        LogUtil.d("getCheckGiftCardUrl--------->" + url);
//        return url;
//    }
//
//    private String getAuthUrl(String param) {
//        return AuthManager.GetInstance().getAuthServer() + "/query?reqinfo=<req><data>" + param + "</data></req>";
//    }
//
//    private String getSequenceNo() {
//        String sequenceno = null;
//        long time = System.currentTimeMillis();
//        final Calendar mCalendar = Calendar.getInstance();
//        mCalendar.setTimeInMillis(time);
//        int mYear = mCalendar.get(Calendar.YEAR);
//        int mHour = mCalendar.get(Calendar.HOUR);
//        int mMinutes = mCalendar.get(Calendar.MINUTE);
//        int mSeconds = mCalendar.get(Calendar.SECOND);
//        int mMiliSeconds = mCalendar.get(Calendar.MILLISECOND);
//        sequenceno = String.format("%s_%4d%02d%02d%02d%07d", "10000101", mYear, mHour, mMinutes, mSeconds,
//                mMiliSeconds);
//        return sequenceno;
//    }
//
//    public void showLog(String messsage) {
//        if (DEBUG)
//            Log.d(TAG, messsage);
//    }
//
//    /**
//     * 播放查询接口
//     *
//     * @param aid
//     * @param mclassify
//     * @param sid
//     * @param msid
//     * @param mtype
//     * @param cpid
//     * @param mid
//     * @return
//     */
///*	public PlayInfo getPlayInfo(String aid, String mclassify, String sid, String msid, String mtype, String cpid,
//            String mid) {
//		PlayInfo ret = new PlayInfo();
//		int retryTimes = 5;
//		for (int i = 0; i < retryTimes; i++) {
//			PlayCheckInfo playCheckInfo = null;
//			playCheckInfo = getPlayCheckInfo(aid, mclassify, sid, msid, mtype, cpid, mid);
//			if (playCheckInfo != null) {
//				ret.setStatus(playCheckInfo.getStatus());
//				ret.setResultDesc(playCheckInfo.getResultDesc());
//				if (!"0".equals(playCheckInfo.getStatus()) || playCheckInfo.getProductList() == null
//						|| playCheckInfo.getProductList().size() < 1) {
//					return ret;
//				}
//				// ret.setProductList(playCheckInfo.getProductList());
//				LogUtil.d("getPlayInfo----->");
//				if (playCheckInfo.getPfilmList() != null && playCheckInfo.getPfilmList().size() > 0
//						&& playCheckInfo.getPfilmList().get(0).fsproductList != null
//						&& playCheckInfo.getPfilmList().get(0).fsproductList.size() > 0) {
//					Fsproduct fsproduct = playCheckInfo.getPfilmList().get(0).fsproductList.get(0);
//					ret.setProductList(getProductListFromPidList(playCheckInfo.getProductList(), fsproduct.plist));
//					ret.setCurrentSinglePlayProduct(getProductFromPid(playCheckInfo.getProductList(), fsproduct.sgpid));
//					if ("0".equals(fsproduct.viewed)) {
//						ret.setOrderInfo(OrderInfo.VIEWED);
//						ret.setCurrentPlayProduct(getProductFromPid(playCheckInfo.getProductList(), fsproduct.pid));
//						ret.setEndTime(fsproduct.etime);
//					} else if ("0".equals(fsproduct.order)) {
//						ret.setOrderInfo(OrderInfo.ORDERED);
//						ret.setCurrentPlayProduct(getProductFromPid(playCheckInfo.getProductList(), fsproduct.pid));
//						ret.setEndTime(fsproduct.etime);
//					} else {
//						Product p = getProductFromPid(playCheckInfo.getProductList(), fsproduct.sgpid);
//						if(p != null){
//							if ("0".equals(p.getFee())) {
//								ret.setOrderInfo(OrderInfo.FREE);
//							} else {
//								ret.setOrderInfo(OrderInfo.CHARGE);
//							}
//							ret.setCurrentPlayProduct(p);
//						}else{
//							ret.setOrderInfo(OrderInfo.CHARGE);
//							if (fsproduct.plist != null && fsproduct.plist.size() > 0) {
//								ret.setCurrentPlayProduct(getProductFromPid(playCheckInfo.getProductList(), fsproduct.plist.get(0)));
//							} else {
//								ret.setCurrentPlayProduct(null);
//							}
//						}
//					}
//				} else {
//					ret.setProductList(playCheckInfo.getProductList());
//					ret.setCurrentSinglePlayProduct(playCheckInfo.getProductList().get(0));
//					if ("0".equals(playCheckInfo.getProductList().get(0).getFee())) {
//						ret.setOrderInfo(OrderInfo.FREE);
//					} else {
//						ret.setOrderInfo(OrderInfo.CHARGE);
//					}
//					ret.setCurrentPlayProduct(playCheckInfo.getProductList().get(0));
//				}
//				return ret;
//			} else {
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				AuthManager.GetInstance().startAuth();
//				AuthManager.GetInstance().getUser();
//				AuthManager.GetInstance().getUrlMap();
//				ret.setStatus("1");
//				ret.setOrderDescription("获取影片产品信息出错");
//			}
//		}
//		return ret;
//
//	}*/
//    public PlayInfo getPlayInfoNew(String aid, String mclassify, String sid, String msid, String mtype, String cpid,
//                                   String mid) {
//        PlayInfo ret = new PlayInfo();
//        int retryTimes = 5;
//        for (int i = 0; i < retryTimes; i++) {
//            PlayCheckInfo playCheckInfo = getPlayCheckInfo(aid, mclassify, sid, msid, mtype, cpid, mid);
//            if (playCheckInfo != null) {
//                ret.setStatus(playCheckInfo.getStatus());
//                ret.setResultDesc(playCheckInfo.getResultDesc());
//                if (!"0".equals(playCheckInfo.getStatus()) || playCheckInfo.getProductList() == null
//                        || playCheckInfo.getProductList().size() < 1) {
//                    return ret;
//                }
//                LogUtil.d("getPlayInfoNew----->");
//                Product product = playCheckInfo.getProductList().get(0);
//                if ("0".equals(product.getFee())) {
//                    ret.setOrderInfo(OrderInfo.FREE);
//                    ret.setCurrentPlayProduct(product);
//                } else {
//                    if ("1".equals(product.getIsOrder())) {
//                        ret.setOrderInfo(OrderInfo.ORDERED);
//                        ret.setCurrentPlayProduct(product);
//                        ret.setEndTime(product.getStopTime());
//                    } else if ("0".equals(product.getIsOrder())) {
//                        ret.setOrderInfo(OrderInfo.CHARGE);
//                        ret.setCurrentPlayProduct(product);
//                        ret.setProductList(playCheckInfo.getProductList());
//                    }
//
//                }
//                break;
//            } else {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (!AuthManager.GetInstance().isAuthRunning()) {
//                    AuthManager.GetInstance().startAuth();
//                    AuthManager.GetInstance().getUser();
//                    AuthManager.GetInstance().getUrlMap();
//                }
//                ret.setStatus("1");
//                ret.setOrderDescription("获取影片产品信息出错");
//            }
//        }
//        return ret;
//    }
//
//    private List<Product> getProductListFromPidList(List<Product> productList, List<String> pidList) {
//        List<Product> retProductList = new ArrayList<Product>();
//        for (int i = 0; i < pidList.size(); i++) {
//            Product p = getProductFromPid(productList, pidList.get(i));
//            retProductList.add(p);
//        }
//        return retProductList;
//    }
//
//    private Product getProductFromPid(List<Product> productList, String pid) {
//        Product product = null;
//        int size = productList.size();
//        if (size > 0) {
//            for (int i = 0; i < productList.size(); i++) {
//                if (pid.equals(productList.get(i).getPid())) {
//                    product = productList.get(i);
//                    return product;
//                }
//            }
//        }
//
//        return product;
//    }
//
//    private PlayCheckInfo getPlayCheckInfo(String aid, String mclassify, String sid, String msid, String mtype,
//                                           String cpid, String mid) {
//        String url = getPlayCheckInfoUrl(aid, mclassify, sid, msid, mtype, cpid, mid);
//        // String url = getPlayCheckInfoUrlTest(aid, mclassify, sid,
//        // msid,mtype,cpid, mid);
//        PlayCheckInfoNewParser parser = new PlayCheckInfoNewParser();
//        //	PlayCheckInfoParser parser = new PlayCheckInfoParser();
//        try {
//            parser.setUrl(url);
//            return parser.getInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("PlayCheckInfo--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    private String getPlayCheckInfoUrl(String aid, String mclassify, String sid, String msid, String mtype, String cpid,
//                                       String mid) {
//        StringBuilder sb = new StringBuilder(AuthManager.GetInstance().getAuthServer());
//        sb.append("/query?reqinfo=<req><data>{\"action\":\"preplayquery\"");
//        sb.append(",\"aid\":");
//        sb.append("\"");
//        sb.append(aid);
//        sb.append("\"");
//        sb.append(",\"mclassify\":");
//        sb.append("\"");
//        sb.append(mclassify);
//        sb.append("\"");
//        sb.append(",\"sid\":");
//        sb.append("\"");
//        sb.append(sid);
//        sb.append("\"");
//        sb.append(",\"msid\":");
//        sb.append("\"");
//        sb.append(msid);
//        sb.append("\"");
//        sb.append(",\"mtype\":");
//        sb.append("\"");
//        sb.append(mtype);
//        sb.append("\"");
//        sb.append(",\"cpid\":");
//        sb.append("\"");
//        sb.append(cpid);
//        sb.append("\"");
//        sb.append(",\"mid\":");
//        sb.append("\"");
//        sb.append(mid);
//        sb.append("\"");
//        sb.append(",\"userid\":");
//        sb.append("\"");
//        sb.append(VUserManager.getInstance().getVUser().getUserid());
//        sb.append("\"");
//        sb.append(",\"token\":");
//        sb.append("\"");
//        sb.append(VUserManager.getInstance().getToken());
//        sb.append("\"");
//        sb.append(",\"format\":\"0\"");
//        sb.append(",\"version\":\"6.0\"");
//        sb.append("}</data></req>");
//        LogUtil.d("getPlayCheckInfo-->" + sb.toString());
//        return sb.toString();
//    }
//
//    public String getMobilRechargeUrl(String price) {
//        String url = getUniPayUrl() + "&price=" + price + "&responseformat=xml&Datetime=" + DateUtil.getDateTime()
//                + "&ordertype=0";
//        LogUtil.d("getMobilRechargeUrl--------->" + url);
//        return url;
//    }
//
//    /**
//     * 获取短连接url
//     *
//     * @param url
//     * @return
//     */
//
//    public String getShortLink(String url) {
//        String linkshortUrl = null;
//        if (AuthManager.GetInstance().getUrlMap() != null
//                && !TextUtils.isEmpty(UrlManager.getInstance().getDLL(Key.KEY_LINK_SHORT))) {
//            linkshortUrl = UrlManager.getInstance().getDLL(Key.KEY_LINK_SHORT);
//            if (linkshortUrl.contains("?")) {
//                linkshortUrl = linkshortUrl.substring(0, linkshortUrl.indexOf("?"));
//            }
//            LogUtil.d("linkshortUrl---->" + linkshortUrl);
//        } else {
//            return null;
//        }
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("version", "1.0");
//            jsonObject.put("longlink", url);
//            jsonObject.put("source", "0001");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        LogUtil.d("json:" + jsonObject.toString());
//        StringBuffer httpMessage = new StringBuffer();
//        if (NetUtil.doPost(linkshortUrl, jsonObject.toString(), httpMessage)) {
//            try {
//                JSONObject object = new JSONObject(httpMessage.toString());
//                String err_msg = object.getString("err_msg");
//                String err_code = object.getString("err_code");
//                String shortlink = object.getString("shortlink");
//                if ("0".equals(err_code)) {
//                    return shortlink;
//                } else {
//                    LogUtil.d("getShortLink --> err_msg:" + err_msg);
//                    return null;
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            return null;
//        }
//        return null;
//    }
//
//    public Bitmap getMobilePayQRImage(String appid, String aid, String mclassify, String sid, String mid, String cpid, String title,
//                                      String mtype, String price, String pid, String ptype, String msid, String pname, int width, int height) {
//        String url = getMobilePayUrl(appid, aid, mclassify, sid, mid, cpid, title, pid, msid, ptype, pname, price);
//        return QRCodeUtil.createImage(url, width, height);
//    }
//
//
//    private String getUniPayUrl1() {
//        String url = UrlManager.getInstance().getDLL(Key.KEY_UNI_PAY) + "&issendxmpp=1";
//        LogUtil.d("getUniPayUrl--------->" + url);
//        return url;
//    }
//
//    /**
//     * 用户产品信息查询
//     *
//     * @return
//     */
//    public ProductListInfo getUserProduct(String groupId) {
//        String url = "";
//        if (IS_USE_AUTH) {
//            url = getUserProductUrlForAuth();
//        } else {
//            url = getUserProductUrl(groupId);
//        }
//        ProductParse parser = new ProductParse();
//        try {
//            parser.setUrl(url);
//            return parser.getList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("getUserProduct--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 用户产品信息查询url
//     *
//     * @return
//     */
//    private String getUserProductUrl(String groupId) {
//        StringBuilder sb = new StringBuilder(getCommonUrl());
//        sb.append("&action=userproduct");
//        sb.append("&groupid=" + groupId);
//        sb.append("&userid=" + VUserManager.getInstance().getVUser().getUserid());
//        sb.append("&token=" + VUserManager.getInstance().getToken());
//        sb.append("&version=6.0");
//        String url = sb.toString();
//        LogUtil.d("getUserProductUrl--------->" + url);
//        return url;
//    }
//
//    /**
//     * 用户产品信息查询url 通过auth
//     *
//     * @return
//     */
//    private String getUserProductUrlForAuth() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"userproduct\",");
//        sb.append("\"sequenceno\":\"" + getSequenceNo() + "\",");
//        sb.append("\"datetime\":\"" + DateUtil.getDateTime() + "\"}");
//        String url = getAuthUrl(sb.toString());
//        LogUtil.d("getUserProductUrlForAuth--------->" + url);
//        return url;
//    }
//
//    /**
//     * 普通充值卡充值
//     *
//     * @param cardNo
//     * @return
//     */
//    public RechargeResult recharge(String cardNo) {
//        return recharge(cardNo, ORDER_TYPE_RECHARGE);
//    }
//
//    /**
//     * 充值卡充值
//     *
//     * @param cardNo
//     * @return
//     */
//    public RechargeResult recharge(String cardNo, String orderfrom) {
//        String url = "";
//        if (IS_USE_AUTH) {
//            url = getRechargeUrlForAuth(cardNo, orderfrom);
//        } else {
//            url = getRechargeUrl(cardNo, orderfrom);
//        }
//        LogUtil.d("recharge--------->" + url);
//        RechargeResultParser parser = new RechargeResultParser();
//        try {
//            parser.setUrl(url);
//            return parser.getResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("recharge--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 获得充值url For Auth
//     *
//     * @param cardNo
//     * @return
//     */
//    private String getRechargeUrlForAuth(String cardNo, String orderfrom) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"userpay\",");
//        sb.append("\"sequenceno\":\"" + getSequenceNo() + "\",");
//        sb.append("\"orderfrom\":\"" + orderfrom + "\",");
//        sb.append("\"version\":\"5.0\",");
//        sb.append("\"cardno\":\"" + cardNo + "\"}");
//        String url = getAuthUrl(sb.toString());
//        return url;
//    }
//
//    /**
//     * 获得充值url
//     *
//     * @param cardNo
//     * @return
//     */
//    private String getRechargeUrl(String cardNo, String orderfrom) {
//        StringBuffer sb = new StringBuffer(getCommonUrl());
//        sb.append("&action=userpay");
//        sb.append("&orderfrom=" + orderfrom);
//        sb.append("&version=5.0");
//        sb.append("&cardno=" + cardNo);
//        return sb.toString();
//    }
//
//    /**
//     * 订购单点
//     *
//     * @param pid
//     * @param pType
//     * @param aid
//     * @param msid
//     * @param mclassify
//     * @param mid
//     * @param sid
//     * @return
//     */
//    public OrderResult orderSingle(String pid, String pType, String aid, String msid, String mclassify, String mid,
//                                   String sid) {
//        String url = "";
//        if (IS_USE_AUTH) {
//            url = getOrderSingleUrlForAuth(pid, pType, aid, msid, mclassify, mid, sid);
//        } else {
//            url = getOrderSingleUrl(pid, pType, aid, msid, mclassify, mid, sid);
//        }
//        LogUtil.d("orderSingle--------->" + url);
//        OrderResultParser orderInfoParser = new OrderResultParser();
//        try {
//            orderInfoParser.setUrl(url);
//            return orderInfoParser.getOrderInfo();
//        } catch (Exception e) {
//            LogUtil.d("order--------->Exception");
//        }
//        return null;
//    }
//
//    /**
//     * 获得订购url For Auth
//     *
//     * @return
//     */
//    private String getOrderSingleUrlForAuth(String pid, String pType, String aid, String msid, String mclassify,
//                                            String mid, String sid) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"userorder\",");
//        sb.append("\"sequenceno\":\"" + getSequenceNo() + "\",");
//        sb.append("\"version\":\"5.0\",");
//        sb.append("\"aid\":\"" + aid + "\",");
//        sb.append("\"msid\":\"" + msid + "\",");
//        sb.append("\"mclassify\":\"" + mclassify + "\",");
//        sb.append("\"mid\":\"" + mid + "\",");
//        sb.append("\"sid\":\"" + sid + "\",");
//        sb.append("\"pid\":\"" + pid + "\",");
//        sb.append("\"feetype\":\"" + pType + "\"}");
//        String url = getAuthUrl(sb.toString());
//        return url;
//    }
//
//    private String getOrderSingleUrl(String pid, String pType, String aid, String msid, String mclassify, String mid,
//                                     String sid) {
//        StringBuffer sb = new StringBuffer(getCommonUrl());
//        sb.append("&action=userorder");
//        sb.append("&version=5.0");
//        sb.append("&aid=" + aid);
//        sb.append("&msid=" + msid);
//        sb.append("&mclassify=" + mclassify);
//        sb.append("&mid=" + mid);
//        sb.append("&sid=" + sid);
//        sb.append("&pid=" + pid);
//        sb.append("&feetype=" + pType);
//        return sb.toString();
//    }
//
//    /**
//     * 订购
//     *
//     * @param product
//     * @return
//     */
//    public OrderResult order(Product product) {
//        String url = "";
//        if (IS_USE_AUTH) {
//            url = getOrderUrlForAuth(product);
//        } else {
//            url = getOrderUrl(product);
//        }
//        LogUtil.d("order--------->" + url);
//        OrderResultParser orderInfoParser = new OrderResultParser();
//        try {
//            orderInfoParser.setUrl(url);
//            return orderInfoParser.getOrderInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("order--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 获得订购url For Auth
//     *
//     * @param product
//     * @return
//     */
//    private String getOrderUrlForAuth(Product product) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"userorder\",");
//        sb.append("\"sequenceno\":\"" + getSequenceNo() + "\",");
//        sb.append("\"version\":\"5.0\",");
//        sb.append("\"pid\":\"" + product.getPid() + "\",");
//        sb.append("\"feetype\":\"" + product.getPtype() + "\"}");
//        String url = getAuthUrl(sb.toString());
//        return url;
//    }
//
//    /**
//     * 获得订购url
//     *
//     * @param product
//     * @return
//     */
//    private String getOrderUrl(Product product) {
//        StringBuffer sb = new StringBuffer(getCommonUrl());
//        sb.append("&action=userorder");
//        sb.append("&version=5.0");
//        sb.append("&pid=" + product.getPid());
//        sb.append("&feetype=" + product.getPtype());
//        return sb.toString();
//    }
//
//    /**
//     * 影片单点价格查询
//     *
//     * @param filmPrices
//     * @return
//     */
//    public List<FilmPrice> getFilmPrices(List<FilmPrice> filmPrices) {
//        String url = getFilmPriceUrl(filmPrices);
//        FilmPriceParser parser = new FilmPriceParser();
//        try {
//            url = url + getFilmPriceBody(filmPrices);
//            Log.i("ABCDEFG", "---------" + url);
//            parser.setUrl(url);
//            LogUtil.d("getFilmPrices--------->" + url);
//            // parser.postUrl(url, getFilmPriceBody(filmPrices));
//            return parser.getFilmPrices();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("orgetFilmPricesder--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 获得查询价格url
//     *
//     * @param filmPrices
//     * @return
//     */
//    private String getFilmPriceUrl(List<FilmPrice> filmPrices) {
//        StringBuilder sb = new StringBuilder(getCommonUrl());
//        sb.append("&action=filmlistprice");
//        String url = sb.toString();
//        return url;
//    }
//
//    /**
//     * 获得post请求body
//     *
//     * @param filmPrices
//     * @return
//     */
//    private String getFilmPriceBody(List<FilmPrice> filmPrices) {
//        // StringBuilder sb = new StringBuilder();
//        // sb.append("{");
//        // sb.append("\"oemid\":\"" +
//        // AuthManager.GetInstance().getUser().getOemid() + "\",");
//        // sb.append("\"uid\":\"" + AuthManager.GetInstance().getUser().getUid()
//        // + "\",");
//        // sb.append("\"hid\":\"" + AuthManager.GetInstance().getUser().getHid()
//        // + "\",");
//        // sb.append("\"movielist\":\"" + change(filmPrices) + "\",");
//        // sb.append("\"version\":\"5.0\"}");
//        // sb.append("}");
//        // String url = sb.toString();
//        // LogUtil.d("getFilmPricePostBody--------->" + url);
//        // return url;
//
//        FilmPricePostBody flmPricePostBody = new FilmPricePostBody();
//        flmPricePostBody.setMovielist(filmPrices);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(flmPricePostBody.getMovielist());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("&movielist=");
//        stringBuilder.append(json);
//        stringBuilder.append("&version=5.0");
//        return stringBuilder.toString();
//
//    }
//
//    private class FilmPricePostBody {
//
//        private List<FilmPrice> movielist;
//
//        public List<FilmPrice> getMovielist() {
//            return movielist;
//        }
//
//        public void setMovielist(List<FilmPrice> movielist) {
//            this.movielist = movielist;
//        }
//
//    }
//
//    private String getCommonUrl() {
//        String url = UrlManager.getInstance().getDLL(Key.KEY_ACCOUNT) + "&datetime=" + DateUtil.getDateTime()
//                + "&sequenceno=" + getSequenceNo() + "&apptype=" + appType;
//        LogUtil.d("getCommonUrl--------->" + url);
//        return url;
//    }
//
//    /**
//     * 获得消费记录
//     *
//     * @return
//     */
//    public ConsumeListInfo getConsumeInfo(int page, int pageSize) {
//        String url = "";
//        if (IS_USE_AUTH) {
//            url = getConsumeListUrlForAuth(page, pageSize);
//        } else {
//            url = getConsumeListUrl(page, pageSize);
//        }
//        LogUtil.d("getConsumeInfo--------->" + url);
//        ConsumeListInfoParser parser = new ConsumeListInfoParser();
//        try {
//            parser.setUrl(url);
//            return parser.getConsumeListInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("getConsumeInfo--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 获得消费记录url 通过auth
//     *
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    private String getConsumeListUrlForAuth(int page, int pageSize) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"revenueinfo\",");
//        sb.append("\"p\":\"" + page + "\",");
//        sb.append("\"psize\":\"" + pageSize + "\",");
//        sb.append("\"sequenceno\":\"" + getSequenceNo() + "\",");
//        sb.append("\"datetime\":\"" + DateUtil.getDateTime() + "\"}");
//        String url = getAuthUrl(sb.toString());
//        LogUtil.d("getConsumeListUrlForAuth--------->" + url);
//        return url;
//    }
//
//    /**
//     * 获得消费记录url
//     *
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    private String getConsumeListUrl(int page, int pageSize) {
//        StringBuffer sb = new StringBuffer(getCommonUrl());
//        sb.append("&action=revenueinfo");
//        sb.append("&p=");
//        sb.append(page + "");
//        sb.append("&psize=");
//        sb.append(pageSize + "");
//        sb.append("&action=revenueinfo");
//        sb.append("&format=0");
//        sb.append("&userid=" + VUserManager.getInstance().getVUser().getUserid());
//        sb.append("&token=" + VUserManager.getInstance().getToken());
//        sb.append("&version=6.0");
//        String url = sb.toString();
//        LogUtil.d("getConsumeListUrl--------->" + url);
//        return url;
//    }
//
//    /**
//     * 获得用户信息及订购信息
//     *
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    public OrderListInfo getOrderInfo(int page, int pageSize) {
//        String url = "";
//        if (IS_USE_AUTH) {
//            url = getOrderListUrlForAuth(page, pageSize);
//        } else {
//            url = getOrderListUrl(page, pageSize);
//        }
//        LogUtil.d("getOrderInfo--------->" + url);
//        OrderListInfoParser parser = new OrderListInfoParser();
//        try {
//            parser.setUrl(url);
//            return parser.getOrderListInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("getOrderInfo--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 获得用户信息以及订购信息 通过auth
//     *
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    private String getOrderListUrlForAuth(int page, int pageSize) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"userinfo\",");
//        sb.append("\"nodetype\":\"1\",");
//        sb.append("\"p\":\"1\",");
//        sb.append("\"psize\":\"10000\",");
//        sb.append("\"sequenceno\":\"" + getSequenceNo() + "\",");
//        sb.append("\"datetime\":\"" + DateUtil.getDateTime() + "\"}");
//        String url = getAuthUrl(sb.toString());
//        LogUtil.d("getOrderListUrlForAuth--------->" + url);
//        return url;
//    }
//
//    /**
//     * 获得用户信息以及订购信息
//     *
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    private String getOrderListUrl(int page, int pageSize) {
//        StringBuffer sb = new StringBuffer(getCommonUrl());
//        sb.append("&action=userinfo");
//        sb.append("&p=" + page);
//        sb.append("&psize=" + pageSize);
//        sb.append("&userid=" + VUserManager.getInstance().getVUser().getUserid());
//        sb.append("&token=" + VUserManager.getInstance().getToken());
//        sb.append("&nodetype=1");
//        sb.append("&format=0");
//        sb.append("&version=6.0");
//        String url = sb.toString();
//        LogUtil.d("getOrderListUrl--------->" + url);
//        return url;
//    }
//
//    /**
//     * 获得充值记录
//     *
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    public RechargeListInfo getRechargeInfo(int page, int pageSize) {
//        String url = getRechargeListUrl(page, pageSize);
//        RechargeListInfoParser parser = new RechargeListInfoParser();
//        try {
//            parser.setUrl(url);
//            return parser.getRechargeListInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("getRechargeInfo--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 获得充值记录url
//     *
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    private String getRechargeListUrl(int page, int pageSize) {
//        StringBuffer sb = new StringBuffer(getCommonUrl());
//        sb.append("&action=userinfo");
//        sb.append("&p=");
//        sb.append(page + "");
//        sb.append("&psize=");
//        sb.append(pageSize + "");
//        sb.append("&nodetype=2");
//        sb.append("&version=1.0");
//        String url = sb.toString();
//        LogUtil.d("getRechargeListUrl--------->" + url);
//        return url;
//    }
//
//    /**
//     * 获得订购产品订单
//     *
//     * @param price
//     * @param pid
//     * @param ptype
//     * @param orderFrom
//     * @return
//     */
//    public GetOrderResult getProductOrderNo(String price, String pid, String ptype, String orderFrom) {
//        return getOrderNo(ORDER_TYPE_PRODUCT, orderFrom, price, null, null, null, null, null, pid, ptype, null, null);
//    }
//
//    /**
//     * 获得单点产品订单
//     *
//     * @param price
//     * @param pid
//     * @param ptype
//     * @param orderFrom
//     * @return
//     */
//    public GetOrderResult getFilmOrderNo(String price, String cpid, String aid, String msid, String mid,
//                                         String mclassify, String sid, String title, String pid, String ptype, String orderFrom) {
//        return getOrderNo(ORDER_TYPE_FILM, orderFrom, price, aid, msid, mid, mclassify, sid, pid, ptype, title, cpid);
//    }
//
//    private GetOrderResult getOrderNo(String orderType, String orderFrom, String price, String aid, String msid,
//                                      String mid, String mclassify, String sid, String pid, String ptype, String title, String cpid) {
//        // String url = "";
//        // if (IS_USE_AUTH) {
//        String url = getOrderNoUrlForAuth(orderType, orderFrom, price, aid, msid, mid, mclassify, sid, pid, ptype,
//                title, cpid);
//        // } else {
//        // url = getOrderNoUrl(orderType, orderFrom, price, aid, msid, mid,
//        // mclassify, sid, pid, ptype, title);
//        // }
//        LogUtil.d("getOrderNo--------->" + url);
//        GetOrderResultParser parser = new GetOrderResultParser();
//        try {
//            parser.setUrl(url);
//            return parser.getOrderResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("getOrderNo--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    private String getOrderNoUrl(String orderType, String orderFrom, String price, String aid, String msid, String mid,
//                                 String mclassify, String sid, String pid, String ptype, String title) {
//        StringBuffer sb = new StringBuffer(getCommonUrl());
//        sb.append("&checkservice=1&action=orderinfo");
//        sb.append("&ordertype=" + orderType);
//        sb.append("&price=" + price);
//        if (!TextUtils.isEmpty(aid)) {
//            sb.append("&aid=" + aid);
//        }
//        if (!TextUtils.isEmpty(orderFrom)) {
//            sb.append("&orderfrom=" + orderFrom);
//        }
//        if (!TextUtils.isEmpty(msid)) {
//            sb.append("&msid=" + msid);
//        }
//
//        if (!TextUtils.isEmpty(mid)) {
//            sb.append("&mid=" + mid);
//        }
//
//        if (!TextUtils.isEmpty(sid)) {
//            sb.append("&sid=" + sid);
//        }
//
//        if (!TextUtils.isEmpty(mclassify)) {
//            sb.append("&mclassify=" + mclassify);
//        }
//
//        if (!TextUtils.isEmpty(pid)) {
//            sb.append("&pid=" + pid);
//        }
//
//        if (!TextUtils.isEmpty(ptype)) {
//            sb.append("&ptype=" + ptype);
//        }
//
//        if (!TextUtils.isEmpty(title)) {
//            sb.append("&title=" + title);
//        }
//        sb.append("&version=5.0");
//        return sb.toString();
//    }
//
//    private String getOrderNoUrlForAuth(String orderType, String orderFrom, String price, String aid, String msid,
//                                        String mid, String mclassify, String sid, String pid, String ptype, String title, String cpid) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"orderinfo\",");
//        sb.append("\"ordertype\":\"" + orderType + "\",");
//        sb.append("\"price\":\"" + price + "\",");
//        if (aid != null) {
//            sb.append("\"aid\":\"" + aid + "\",");
//        }
//        if (orderFrom != null) {
//            sb.append("\"orderfrom\":\"" + orderFrom + "\",");
//        }
//        if (msid != null) {
//            sb.append("\"msid\":\"" + msid + "\",");
//        }
//
//        if (mid != null) {
//            sb.append("\"mid\":\"" + mid + "\",");
//        }
//
//        if (sid != null) {
//            sb.append("\"sid\":\"" + sid + "\",");
//        }
//
//        if (mclassify != null) {
//            sb.append("\"mclassify\":\"" + mclassify + "\",");
//        }
//
//        if (pid != null) {
//            sb.append("\"pid\":\"" + pid + "\",");
//        }
//
//        if (ptype != null) {
//            sb.append("\"ptype\":\"" + ptype + "\",");
//        }
//
//        if (title != null) {
//            sb.append("\"title\":\"" + title + "\",");
//        }
//
//        if (cpid != null) {
//            sb.append("\"cpid\":\"" + cpid + "\",");
//        }
//        sb.append("\"checkservice\":\"1\",");
//        sb.append("\"version\":\"5.0\",");
//        sb.append("\"datetime\":\"" + DateUtil.getDateTime() + "\"}");
//        String url = getAuthUrl(sb.toString());
//        return url;
//    }
//
//    private ICooprationAccountListener cooprationAccountListener = null;
//
//    public void setCooprationAccountListener(ICooprationAccountListener cooprationAccountListener) {
//        this.cooprationAccountListener = cooprationAccountListener;
//    }
//
//    public List<String> getCooprationProductIdList() {
//        if (cooprationAccountListener != null) {
//            return cooprationAccountListener.getProductIdList();
//        }
//        return null;
//    }
//
//    public boolean isCooprationLogin() {
//        if (cooprationAccountListener != null) {
//            return cooprationAccountListener.isLoging();
//        }
//        return false;
//    }
//
//    public interface ICooprationAccountListener {
//        public boolean isLoging();
//
//        public List<String> getProductIdList();
//    }
//
//    /**
//     * 单点
//     *
//     * @return
//     */
//    // public String getMobilePayUrl(String mid, String sid, String fid, String
//    // price, String title, String pid,
//    // String ptype, String mtype) {
//    // try {
//    // LogUtil.d("getMobilePayUrl --------------------------------> " + title);
//    // title = URLEncoder.encode(title, "UTF-8");
//    // } catch (Exception e) {
//    // e.printStackTrace();
//    // }
//    // String url = getUniPayUrl() + "&mid=" + mid + "&sid=" + sid + "&fid=" +
//    // fid + "&pid=" + pid + "&ptype=" + ptype
//    // + "&title=" + title + "&price=" + price + "&mtype=" + mtype +
//    // "&responseformat=xml&Datetime="
//    // + DateUtil.getDateTime() + "&ordertype=1";
//    // LogUtil.d("getMobilePayUrl--------->" + url);
//    // return url;
//    // }
//    public OrderListInfo getOrderInfo() {
//        String url = "";
//        if (IS_USE_AUTH) {
//            url = getOrderListUrlForAuth();
//        } else {
//            url = getOrderListUrl();
//        }
//        LogUtil.d("getOrderInfo--------->" + url);
//        OrderListInfoParser parser = new OrderListInfoParser();
//        try {
//            parser.setUrl(url);
//            return parser.getOrderListInfo();
//        } catch (Exception e) {
//            LogUtil.d("getOrderInfo----->Exception");
//        }
//        return null;
//    }
//
//    private String getOrderListUrlForAuth() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"userinfo\",");
//        sb.append("\"nodetype\":\"1\",");
//        sb.append("\"p\":\"1\",");
//        sb.append("\"psize\":\"10000\",");
//        sb.append("\"sequenceno\":\"" + getSequenceNo() + "\",");
//        sb.append("\"datetime\":\"" + DateUtil.getDateTime() + "\"}");
//        String url = getAuthUrl(sb.toString());
//        LogUtil.d("getOrderListUrlForAuth--------->" + url);
//        return url;
//    }
//
//    private String getOrderListUrl() {
//        String url = getCommonUrl() + "&actiontype=1&nodetype=1&p=1&psize=10000";
//        LogUtil.d("getOrderListUrl--------->" + url);
//        return url;
//    }
//
//    public void sendIpToWeiXin(String apptype) {
//        String ip = DeviceUtil.getLocalIpAddress();
//        if (!TextUtils.isEmpty(getWeiXinReportUrl())) {
//            StringBuilder urlBuilder = new StringBuilder(getWeiXinReportUrl());
//            urlBuilder.append("&pdversion=4.0");
//            urlBuilder.append("&apptype=").append(apptype);
//            urlBuilder.append("&ip=").append(ip);
//            urlBuilder.append("&terminal=").append(ACCOUNT_TERMINAL_TYPE_TV);
//            String url = urlBuilder.toString();
//            LogUtil.d("sendIpToWeiXin--------->" + url);
//            NetUtil.connectServer(url);
//        }
//    }
//
//    private String getWeiXinReportUrl() {
//        return UrlManager.getInstance().getDLL(Key.KEY_WEIXIN_REPORT);
//    }
//
//    public WeiXinQRItem getWerXinQR() {
//        String url = getWeiXinQRUrl() + "&scene_id=" + AuthManager.GetInstance().getUser().getUid();
//        LogUtil.d("getWerXinQR--------->" + url);
//        StringBuffer buffer = new StringBuffer();
//        if (NetUtil.connectServer(url, buffer)) {
//            WeiXinQRItemParser parser = new WeiXinQRItemParser();
//            parser.parse(buffer.toString());
//            return parser.getItem();
//        }
//        return null;
//    }
//
//    private String getWeiXinQRUrl() {
//        return UrlManager.getInstance().getDLL(Key.KEY_WEIXIN_QRCODE);
//        // return AuthManager.GetInstance().getUrlList().getAccount();
//    }
//
//    /**
//     * 用户信息迁移
//     *
//     * @param informal 游客id
//     * @param formal   正式id
//     */
//    public void identityConvert(String informal, String formal) {
//        String url = UrlManager.getInstance().getDLL(Key.KEY_IDENTITYCONVERT);
//        if (url != null) {
//            StringBuilder urlBuilder = new StringBuilder(url);
//            urlBuilder.append("&informal=").append(informal);
//            urlBuilder.append("&formal=").append(formal);
//            urlBuilder.append("&apptype=").append(appType);
//            url = urlBuilder.toString();
//            LogUtil.d("identityConvert--------->" + url);
//            NetUtil.connectServer(url);
//        }
//
//
//    }
//
//    public ConsumeListInfo getConsumeInfo() {
//        String url = "";
//        if (IS_USE_AUTH) {
//            url = getConsumeListUrlForAuth();
//        } else {
//            url = getConsumeListUrl();
//        }
//        LogUtil.d("getConsumeInfo--------->" + url);
//        ConsumeListInfoParser parser = new ConsumeListInfoParser();
//        try {
//            parser.setUrl(url);
//            return parser.getConsumeListInfo();
//        } catch (Exception e) {
//            LogUtil.d("getConsumeInfo----->Exception");
//        }
//        return null;
//    }
//
//    private String getConsumeListUrlForAuth() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("{\"action\":\"revenueinfo\",");
//        sb.append("\"p\":\"1\",");
//        sb.append("\"psize\":\"10000\",");
//        sb.append("\"sequenceno\":\"" + getSequenceNo() + "\",");
//        sb.append("\"datetime\":\"" + DateUtil.getDateTime() + "\"}");
//        String url = getAuthUrl(sb.toString());
//        LogUtil.d("getConsumeListUrlForAuth--------->" + url);
//        return url;
//    }
//
//    private String getConsumeListUrl() {
//        StringBuilder urlBuilder = new StringBuilder(getCommonUrl());
//        urlBuilder.append("&action=revenueinfo");
//        urlBuilder.append("&p=1");
//        urlBuilder.append("&psize=3");
//        urlBuilder.append("&format=0");
//        urlBuilder.append("&userid=").append(VUserManager.getInstance().getVUser().getUserid());
//        urlBuilder.append("&token=").append(VUserManager.getInstance().getToken());
//        urlBuilder.append("&sequenceno=" + getSequenceNo());
//        urlBuilder.append("&version=6.0");
//        String url = getCommonUrl() + "&actiontype=6&p=1&psize=10000";
//        LogUtil.d("getConsumeListUrl--------->" + url);
//        return url;
//    }
//
//    /**
//     * 影片收藏 选填项传null 获取该影片是否收藏，若已收藏返回已收藏，未收藏则保存数据返回收藏成功
//     *
//     * @param aid    影片专辑ID(必填项)
//     * @param mstype 节目单集类型(必填项)
//     * @param mtype  影片类型[0,电影|1,电视剧](必填项)
//     * @param userid 选填 用户系统userid
//     * @param title  影片名称(URLEncoder.encode(title,"UTF-8"))(选填项)
//     * @param imgurl 影片海报图url (选填项)
//     * @param babyid 小朋友id (选填项)
//     * @return result=2 ，error显示的为错误信息。result=0 , error显示的为成功信息。[0：收藏成功|2：已收藏]
//     */
//    public BaseInfo collectPlayFilm(String aid, String mstype, String mtype, String userid, String title, String imgurl,
//                                    String babyid, String atype) {
//
//        try {
//            // 动态地址
//            String dll = UrlManager.getInstance().getDLL(Key.KEY_COLLECT);
//            if (dll == null) {
//                return null;
//            }
//
//            StringBuilder url = new StringBuilder(dll);
//
//            if (aid != null)
//                url.append("&aid=" + aid);
//            if (mstype != null)
//                url.append("&mstype=" + mstype);
//            if (mtype != null)
//                url.append("&mtype=" + mtype);
//            if (userid != null)
//                url.append("&userid=" + userid);
//            if (title != null)
//                url.append("&title=" + URLEncoder.encode(title, "UTF-8"));
//            if (imgurl != null)
//                url.append("&imgurl=" + imgurl);
//            url.append("&datetime=" + DateUtil.getDateTime());
//            url.append("&version=" + VERSION);
//            if (babyid != null)
//                url.append("&babyid=" + babyid);
//            url.append("&format=0");
//            url.append("&ctype=1");
//            if (atype != null)
//                url.append("&atype=" + atype);
//            url.append("&apptype=" + appType);
//
//            LogUtil.d("collectPlayFilm--url->" + url.toString());
//            FilmCollectParse filmCollectParse = new FilmCollectParse();
//            filmCollectParse.setUrl(url.toString());
//            return filmCollectParse.getBaseInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("collectPlayFilm--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 获取某个用户的收藏列表 选填项传null
//     *
//     * @param userid   选填 用户系统userid
//     * @param page     选填 分页页码，默认1
//     * @param pagesize 选填 每页显示个数，默认12
//     * @param babyid   小朋友id (选填项)
//     */
//    public PlayFilmInfo getCollectFilmList(String userid, int page, int pagesize, String babyid) {
//
//        try {
//            // 动态地址
//            String dll = UrlManager.getInstance().getDLL(Key.KEY_COLLECT);
//            if (dll == null) {
//                return null;
//            }
//            StringBuilder url = new StringBuilder(dll);
//            if (userid != null)
//                url.append("&userid=" + userid);
//            url.append("&page=" + page);
//            url.append("&pagesize=" + pagesize);
//            url.append("&datetime=" + DateUtil.getDateTime());
//            url.append("&version=" + VERSION);
//            if (babyid != null)
//                url.append("&babyid=" + babyid);
//            url.append("&format=0");
//            url.append("&ctype=2");
//            url.append("&apptype=" + appType);
//
//            LogUtil.d("getCollectFilmList--url->" + url.toString());
//            // Log.d("tag", url.toString());
//            PlayInfoParse playInfoParse = new PlayInfoParse();
//            playInfoParse.setUrl(url.toString());
//            // PlayFilmInfo playFilmInfo = playInfoParse.getPlayFilmInfo();
//            return playInfoParse.getPlayFilmInfo();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("getCollectFilmList--url->+Exception:  " + e.getMessage());
//
//        }
//        return null;
//    }
//
//    /**
//     * 删除某个用户的收藏记录 说明通过aid删除用户的收藏记录，aid为空删除全部收藏记录 选填项传null
//     *
//     * @param userid 选填 用户系统userid
//     * @param aid    影片mid，指定删除某个收藏的影片 (选填项),支持多个同时删除（aid=123,111,222）
//     * @param mstype 节目单集类型 (选填项)
//     * @param babyid 小朋友id (选填项)
//     * @return Result = 2 ， error显示的为错误信息Result = 0 ， error显示的为成功信息
//     */
//    public BaseInfo deleteCollectFilmInfo(String userid, String aid, String mstype, String babyid, String atype) {
//        try {
//            // 动态地址
//            String dll = UrlManager.getInstance().getDLL(Key.KEY_COLLECT);
//            if (dll == null) {
//                return null;
//            }
//
//            StringBuilder url = new StringBuilder(dll);
//            if (userid != null)
//                url.append("&userid=" + userid);
//            if (aid != null)
//                url.append("&aid=" + aid);
//            if (mstype != null)
//                url.append("&mstype=" + mstype);
//
//            url.append("&datetime=" + DateUtil.getDateTime());
//            url.append("&version=" + VERSION);
//            if (babyid != null)
//                url.append("&babyid=" + babyid);
//            if (atype != null)
//                url.append("&atype=" + atype);
//            url.append("&format=0");
//            url.append("&ctype=3");
//            url.append("&apptype=" + appType);
//
//            LogUtil.d("deleteCollectFilmInfo--url->" + url.toString());
//
//            FilmCollectParse filmCollectParse = new FilmCollectParse();
//            filmCollectParse.setUrl(url.toString());
//            return filmCollectParse.getBaseInfo();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("deleteCollectFilmInfo--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//
//    }
//
//    /**
//     * 影片是否收藏 选填项传null 说明通过mid查询该影片是否收藏，若收藏 返回已收藏 否则未收藏
//     *
//     * @param aid    影片专辑ID(必填项)
//     * @param userid 选填 用户系统userid
//     * @param mstype 节目单集类型(必填项)
//     * @param mtype  影片类型[0,电影|1,电视剧](必填项)
//     * @param title  影片名称(URLEncoder.encode(title,"UTF-8"))(选填项)
//     * @param imgurl 影片海报图url (选填项)
//     * @param babyid 小朋友id (选填项)
//     * @return result = 0 ， error=1表示已收藏 result = 0 ， error=0表示未收藏
//     */
//    public BaseInfo collectPlayFilmState(String aid, String userid, String mstype, String mtype, String title,
//                                         String imgurl, String babyid) {
//
//        try {
//            // 动态地址
//            String dll = UrlManager.getInstance().getDLL(Key.KEY_COLLECT);
//            if (dll == null) {
//                return null;
//            }
//            StringBuilder url = new StringBuilder(dll);
//            if (aid != null)
//                url.append("&aid=" + aid);
//            if (userid != null)
//                url.append("&userid=" + userid);
//            if (mstype != null)
//                url.append("&mstype=" + mstype);
//            if (mtype != null)
//                url.append("&mtype=" + mtype);
//            if (title != null)
//                url.append("&title=" + URLEncoder.encode(title, "UTF-8"));
//            if (imgurl != null)
//                url.append("&imgurl=" + imgurl);
//            url.append("&datetime=" + DateUtil.getDateTime());
//            url.append("&version=" + VERSION);
//            if (babyid != null)
//                url.append("&babyid=" + babyid);
//            url.append("&format=0");
//            url.append("&ctype=4");
//            url.append("&apptype=" + appType);
//            LogUtil.d("collectPlayFilmState--url->" + url.toString());
//
//            FilmCollectParse filmCollectParse = new FilmCollectParse();
//            filmCollectParse.setUrl(url.toString());
//            BaseInfo baseInfo = filmCollectParse.getBaseInfo();
//            baseInfo.setNewResultDesc(baseInfo.getResultDesc());
//            return baseInfo;
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("collectPlayFilmState--url->+Exception:  " + e.getMessage());
//        }
//        return null;
//
//    }
//
//    public MessageInfoResult getMessageInfo() {
//        String url = UrlManager.getInstance().getDLL(Key.KEY_TVCS_MESSAGE);
//        LogUtil.d("getMessageInfo--------->" + url);
//        MessageInfoResultParser parser = new MessageInfoResultParser();
//        try {
//            parser.setUrl(url);
//            return parser.getResult();
//        } catch (Exception e) {
//            LogUtil.d("getMessageInfo----->Exception");
//        }
//        return null;
//    }
//
//    public MessageInfoResult getUnReadMessage(String lastid) {
//        String url = UrlManager.getInstance().getDLL(Key.KEY_TVCS_MESSAGE) + "&lastid=" + lastid;
//        LogUtil.d("getUnReadMessageCount--------->" + url);
//        MessageInfoResultParser parser = new MessageInfoResultParser();
//        try {
//            parser.setUrl(url);
//            return parser.getResult();
//        } catch (Exception e) {
//            LogUtil.d("getUnReadMessageCount----->Exception");
//        }
//        return null;
//    }
//
//
//    private String createNoncestr() {
//        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        String res = "";
//        for (int i = 0; i < 32; i++) {
//            Random rd = new Random();
//            res += chars.charAt(rd.nextInt(chars.length() - 1));
//        }
//        return res;
//    }
//
//    private String obtainDateByMap(Map<String, String> map) {
//        StringBuffer buffer = new StringBuffer();
//        Set<String> keySet = map.keySet();
//        Iterator<String> iter = keySet.iterator();
//        while (iter.hasNext()) {
//            String key = iter.next();
//            buffer.append("&" + key + "=" + map.get(key));
//        }
//        return buffer.toString();
//    }
//
//
//    private String getDataString() {
//        Calendar cale = Calendar.getInstance();
//        Date tasktime = cale.getTime();
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
//        return df.format(tasktime);
//    }
//
//
//    public String getMobileOrderUrl(String pid, String ptype, String pname
//            , String price, String appid) {
//        String url = getUniPayUrl();
//        String[] urlSplit = url.split("\\?");
//        Map<String, String> map = new TreeMap<String, String>();
//        if (url.length() > 1) {
//            String[] keySqit = urlSplit[1].split("&");
//            for (String key : keySqit) {
//                String[] mapSqit = key.split("=");
//                if (!"app_version".equals(mapSqit[0])) {
//                    map.put(mapSqit[0], mapSqit[1]);
//                }
//            }
//        }
//        map.put("ip", DeviceUtil.getLocalIpAddress());
//        map.put("userid", VUserManager.getInstance().getVUser().getUserid());
//        map.put("token", VUserManager.getInstance().getToken());
//        map.put("ordertype", ORDER_TYPE_PRODUCT);
//        map.put("orderfrom", "19");
//        map.put("pid", pid);
//        map.put("ptype", ptype);
//        try {
//            pname = URLEncoder.encode(pname, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        map.put("pname", pname);
//        map.put("responseformat", "0");
//        map.put("version", "1.0");
//        map.put("serviceId", UrlManager.getInstance().getDLL(Key.KEY_UNIPAY_SERVICEID));
//        map.put("appid", appid);
//        map.put("timeStamp", getDataString());
//        map.put("nonceStr", createNoncestr());
//        map.put("orderName", pname);
//        map.put("paymentType", "2");
//        map.put("totalFee", price);
//        String signMapString = obtainDateByMap(map);
//        String signTemp = signMapString.substring(1, signMapString.length());
//        try {
//            String sign = signTemp + "&key=" + UrlManager.getInstance().getDLL(Key.KEY_UNIPAY_SIGNKEY);
//            ;
//            sign = URLDecoder.decode(sign, "UTF-8");
//            LogUtil.d("sign--------->" + sign);
//            url = urlSplit[0] + "?" + signTemp + "&sign=" + getMD5(sign).toUpperCase();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        LogUtil.d("getMobilePayUrl--------->" + url);
//        return url;
//    }
//
//    /**
//     * 单点支付
//     *
//     * @param aid
//     * @param mclassify
//     * @param sid
//     * @param mid
//     * @param cpid
//     * @param title
//     * @param pid
//     * @param ptype
//     * @param msid
//     * @return
//     */
//    public String getMobilePayUrl(String appid, String aid, String mclassify, String sid, String mid, String cpid,
//                                  String title, String pid, String msid, String ptype, String pname, String price) {
//        try {
//            LogUtil.d("getMobilePayUrl --------------------------------> " + title);
//            title = URLEncoder.encode(title, "UTF-8");
//            pname = URLEncoder.encode(pname, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("getMobilePayUrl--url->+Exception:  " + e.getMessage());
//        }
//        String url = getUniPayUrl();
//        String[] urlSplit = url.split("\\?");
//        Map<String, String> map = new TreeMap<String, String>();
//        if (url.length() > 1) {
//            String[] keySqit = urlSplit[1].split("&");
//            for (String key : keySqit) {
//                String[] mapSqit = key.split("=");
//                if (!"app_version".equals(mapSqit[0])) {
//                    map.put(mapSqit[0], mapSqit[1]);
//                }
//            }
//        }
//        map.put("responseformat", "0");
//        map.put("version", "1.0");
//        map.put("orderfrom", "19");
//        map.put("ip", DeviceUtil.getLocalIpAddress());
//        map.put("serviceId", UrlManager.getInstance().getDLL(Key.KEY_UNIPAY_SERVICEID));
//        map.put("timeStamp", getDataString());
//        map.put("nonceStr", createNoncestr());
//        map.put("paymentType", "2");
//        map.put("userid", VUserManager.getInstance().getVUser().getUserid());
//        map.put("token", VUserManager.getInstance().getToken());
//        map.put("pid", pid);
//        map.put("ptype", ptype);
//        map.put("pname", pname);
//        map.put("appid", appid);
//        map.put("ordertype", ORDER_TYPE_PRODUCT);
//        map.put("aid", aid);
//        map.put("mclassify", mclassify);
//        map.put("sid", sid);
//        map.put("mid", mid);
//        map.put("cpid", cpid);
//        map.put("title", title);
//        map.put("msid", msid);
//        map.put("orderName", pname);
//        map.put("totalFee", price);
//        String signMapString = obtainDateByMap(map);
//        String signTemp = signMapString.substring(1, signMapString.length());
//        try {
//            String sign = signTemp + "&key=" + UrlManager.getInstance().getDLL(Key.KEY_UNIPAY_SIGNKEY);
//            sign = URLDecoder.decode(sign, "UTF-8");
//            LogUtil.d("sign--------->" + sign);
//            url = urlSplit[0] + "?" + signTemp + "&sign=" + getMD5(sign).toUpperCase();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        LogUtil.d("getMobilePayUrl------getSinglePayUrl--->" + url);
//        return url;
//    }
//
//    private String getUniPayUrl() {
//        String url = UrlManager.getInstance().getDLL(Key.KEY_UNI_PAY);
//        return url.toString();
//    }
//
//
//    private static String getMD5(String val) {
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(val.getBytes());
//            byte[] m = md5.digest();// 加密
//            return getString(m);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return "";
//
//    }
//
//    private static String getString(byte[] b) {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < b.length; i++) {
//            if (Integer.toHexString(0xff & b[i]).length() == 1) {
//                sb.append("0").append(Integer.toHexString(0xff & b[i]));
//            } else {
//                sb.append(Integer.toHexString(0xff & b[i]));
//            }
//        }
//        // return sb.toString().toUpperCase(Locale.getDefault());
//        return sb.toString();
//
//    }
//
//}
