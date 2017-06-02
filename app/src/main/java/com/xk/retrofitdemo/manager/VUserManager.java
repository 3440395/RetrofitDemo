//package com.xk.retrofitdemo.manager;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import com.gntv.tv.common.utils.LocalManager;
//import com.gntv.tv.common.utils.LogUtil;
//import com.google.gson.Gson;
//
//import java.security.MessageDigest;
//
///**
// * Created by Administrator on 2017-3-30.
// */
//
//public class VUserManager {
//    /**
//     * 本地存放的userid
//     */
//    private static final String LOCAL_KEY_USER = "upotvUser";
//
//    private static final String VUSER_TERMINAL_TYPE_TV = "2";
//    private static final String VUSER_APPTYPE_TYPE_EPG = "1";
//    private static final String VUSER_TERMINAL_TYPE_MOBILE = "1";
//    private static final String VUSER_APPTYPE_TYPE_WEIXIN = "4";
//    private static final String OPERTYPE_LOGIN_UID = "1";
//    private static final String OPERTYPE_LOGIN_MOBILE = "2";
//    private static final String CHECKTYPE_LOGIN = "2";
//    private static final String KEY_VMC_TRANSFORM = "vmc_transform_url";
//    private static final String KEY_VMC_CHECKTOKEN = "vmc_checktoken_url";
//    private static final String KEY_VMC_LOGIN = "vmc_login_url";
//    private static final String KEY_VMC_ONLINE = "vmc_online_url";
//    private static final String KEY_VMC_LINE = "vmc_offline_url";
//
//    private static final VUserManager instance = new VUserManager();
//
//    private String appType = "";
//    private VUser user = null;
//
//    private String token = "";
//
//    //游客userid
//    private String visitorUserId = "";
//
//    private VUserManager() {
//    }
//
//    public void init(String appType) {
//        this.appType = appType;
//    }
//
//    public static VUserManager getInstance() {
//        return instance;
//    }
//
//    public VUserResult validateLogin(Context context) {
//        String localJson = LocalManager.GetInstance().getLocal(LOCAL_KEY_USER, "");
//        if (!TextUtils.isEmpty(localJson)) {
//            VUserResult localUser = (VUserResult) jsonToObject(localJson, VUserResult.class);
//            if (localUser != null) {
//                VUserResult checkResult = checkToken(localUser.getUser().getUserid(), localUser.getToken());
//                if (checkResult != null && "0".equals(checkResult.getResult())) {
//                    user = localUser.getUser();
//                    token = localUser.getToken();
//                    return checkResult;
//                }
//            }
//        }
//        VUserResult checkResult = checkOnline(context);
//        if (checkResult != null && "0".equals(checkResult.getResult()) && checkResult.getUser() != null) {
//            savaVUser(checkResult);
//            return checkResult;
//        }
//        VUserResult vUserResult = loginByUid(context);
//        if (vUserResult != null && "0".equals(vUserResult.getResult()) && vUserResult.getUser() != null) {
//            savaVUser(vUserResult);
//        }
//        return vUserResult;
//    }
//
//    public VUserResult mobileLogin(Context context, String mobileNumber, String checkcode, boolean hasSelf) {
//        VUserResult result = loginByMobile(context, mobileNumber, checkcode, hasSelf);
//        if (result != null && "0".equals(result.getResult()) && result.getUser() != null) {
//            savaVUser(result);
//        }
//        return result;
//    }
//
//    public VUser checkVUserLogin(Context context) {
//        VUserResult result = checkOnline(context);
//        if (result != null && "0".equals(result.getResult()) && result.getUser() != null) {
//            savaVUser(result);
//            return result.getUser();
//        }
//        return null;
//    }
//
//    public void savaVUser(VUserResult result) {
//        user = result.getUser();
//        token = result.getToken();
//        LocalManager.GetInstance().saveLocal(LOCAL_KEY_USER, beanToJSON(result));
//    }
//
//    public VUserResult loginOut(Context context) {
////        VUserResult logoutResult = logout(context);
//        logout(context);
////        if (logoutResult != null && "0".equals(logoutResult.getResult())) {
//        VUserResult vUserResult = loginByUid(context);
//        if (vUserResult != null && "0".equals(vUserResult.getResult()) && vUserResult.getUser() != null) {
//            savaVUser(vUserResult);
//        }
//
//        return vUserResult;
////        }
////        VUserResult userResult = new VUserResult();
////        userResult.setResult("0");
////        userResult.setToken(token);
////        userResult.setUser(user);
////        return logoutResult;
//    }
//
//    /**
//     * 退出登录后，保存游客身份的userid
//     */
//    private void saveVisitorUserId(VUserResult vUserResult) {
//        if (vUserResult != null) {
//            visitorUserId = vUserResult.getUser().getUserid();
//        }
//    }
//
//    public String getVisitorUserId() {
//        return visitorUserId;
//    }
//
//    public VUser getVUser() {
//        return user;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public VUserResult logout(Context context) {
//        if (AuthManager.GetInstance().getUrlMap() != null) {
//            String dll = AuthManager.GetInstance().getUrlMap().getUrlMap().get(KEY_VMC_LINE);
//            StringBuffer url = new StringBuffer(dll);
//            url.append(getVUserCommon(context.getPackageName(), true));
//            url.append("&opertype=" + OPERTYPE_LOGIN_UID);
//            url.append("&linetype=" + OPERTYPE_LOGIN_UID);
//            url.append("&userid=" + user.getUserid());
//            url.append("&token=" + token);
//            VUserParser parser = new VUserParser();
//            try {
//                LogUtil.d("AccountManager----->logout----->url:" + url.toString());
//                parser.setUrl(url.toString());
//                return parser.getResult();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 游客登录
//     * @param context
//     * @return
//     */
//    public VUserResult loginByUid(Context context) {
//        if (AuthManager.GetInstance().getUrlMap() != null) {
//            String dll = AuthManager.GetInstance().getUrlMap().getUrlMap().get(KEY_VMC_TRANSFORM);
//            StringBuffer url = new StringBuffer(dll);
//            url.append(getVUserCommon(context.getPackageName(), false));
//            url.append("&opertype=" + OPERTYPE_LOGIN_UID);
//            String token = AuthManager.GetInstance().getUser().getUid() + "_terminal";
//            url.append("&token=" + MD5(token).toUpperCase());
//            VUserParser parser = new VUserParser();
//            try {
//                LogUtil.d("AccountManager----->loginByUid----->url:" + url.toString());
//                parser.setUrl(url.toString());
//
//                VUserResult result = parser.getResult();
//                //游客登录
//                saveVisitorUserId(result);
//                return result;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    private VUserResult checkToken(String userid, String token) {
//        return checkToken(userid, token, false, null);
//    }
//
//    private VUserResult checkToken(String userid, String token, boolean hasSelf, Context context) {
//        if (AuthManager.GetInstance().getUrlMap() != null) {
//            String dll = AuthManager.GetInstance().getUrlMap().getUrlMap().get(KEY_VMC_CHECKTOKEN);
//            StringBuffer url = new StringBuffer(dll);
//            if (hasSelf) {
//                if (context != null) {
//                    url.append(getVUserCommon(context.getPackageName(), false));
//                }
//                url.append("&incterm=" + 1);
//            } else {
//                if (context != null) {
//                    url.append(getVUserCommon(context.getPackageName(), false));
//                }
//                url.append("&incterm=" + 0);
//            }
//            url.append("&opertype=" + OPERTYPE_LOGIN_UID);
//            url.append("&userid=" + userid);
//            url.append("&token=" + token);
//            VUserParser parser = new VUserParser();
//            try {
//                LogUtil.d("AccountManager----->checkToken----->url:" + url.toString());
//                parser.setUrl(url.toString());
//                return parser.getResult();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    private VUserResult loginByMobile(Context context, String mobileNumber, String checkcode, boolean hasSelf) {
//        if (AuthManager.GetInstance().getUrlMap() != null) {
//            String dll = AuthManager.GetInstance().getUrlMap().getUrlMap().get(KEY_VMC_LOGIN);
//            StringBuffer url = new StringBuffer(dll);
//            if (context != null) {
//                url.append(getVUserCommon(context.getPackageName(), true));
//            }
//            url.append("&opertype=" + OPERTYPE_LOGIN_MOBILE);
//            url.append("&checktype=" + CHECKTYPE_LOGIN);
//            url.append("&account=" + mobileNumber);
//            url.append("&code=" + checkcode);
//            if (hasSelf) {
//                url.append("&incsup=" + 1);
//            } else {
//                url.append("&incsup=" + 0);
//            }
//            url.append("&protype=" + 0);
//            VUserParser parser = new VUserParser();
//            try {
//                LogUtil.d("AccountManager----->loginByMobile----->url:" + url.toString());
//                parser.setUrl(url.toString());
//                return parser.getResult();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    private VUserResult checkOnline(Context context) {
//        if (AuthManager.GetInstance().getUrlMap() != null) {
//            String dll = AuthManager.GetInstance().getUrlMap().getUrlMap().get(KEY_VMC_ONLINE);
//            StringBuffer url = new StringBuffer(dll);
//            url.append(getVUserCommon(context.getPackageName(), true));
//            VUserParser parser = new VUserParser();
//            try {
//                LogUtil.d("AccountManager--->checkOnline----->url:" + url.toString());
//                parser.setUrl(url.toString());
//                return parser.getResult();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    private String getVUserCommon(String packageName, boolean isWeixin) {
//        StringBuffer url = new StringBuffer();
//        url.append("&packagename=" + packageName);
//        url.append("&terminal=" + VUSER_TERMINAL_TYPE_TV);
//        url.append("&apptype=" + appType);
//        url.append("&timestamp=" + System.currentTimeMillis());
//        return url.toString();
//    }
//
//    private String MD5(String inStr) {
//        MessageDigest md5 = null;
//        try {
//            md5 = MessageDigest.getInstance("MD5");
//        } catch (Exception e) {
//            System.out.println(e.toString());
//            e.printStackTrace();
//            return "";
//        }
//        char[] charArray = inStr.toCharArray();
//        byte[] byteArray = new byte[charArray.length];
//
//        for (int i = 0; i < charArray.length; i++)
//            byteArray[i] = (byte) charArray[i];
//
//        byte[] md5Bytes = md5.digest(byteArray);
//
//        StringBuffer hexValue = new StringBuffer();
//
//        for (int i = 0; i < md5Bytes.length; i++) {
//            int val = ((int) md5Bytes[i]) & 0xff;
//            if (val < 16)
//                hexValue.append("0");
//            hexValue.append(Integer.toHexString(val));
//        }
//
//        return hexValue.toString();
//    }
//
//    private String beanToJSON(Object bean) {
//        return new Gson().toJson(bean);
//    }
//
//    private Object jsonToObject(String json, Class beanClass) {
//        Gson gson = new Gson();
//        Object res = gson.fromJson(json, beanClass);
//        return res;
//    }
//}
