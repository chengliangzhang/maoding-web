/**
 * Created by wrb on 2016/12/6.
 */

var home_forgetPwd={
    init:function(){
        var that=this;
        var options = {};
        options.nextStepUrl = restApi.url_homeRegister_validateCode;
        options.saveUrl = restApi.url_homeForget_changePwd;
        options.getCodeUrl = restApi.url_homeForget_securityCode;
        $('#forgetStepOBox').m_forgetPWD(options);
    }
};