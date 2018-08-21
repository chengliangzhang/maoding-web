//----------------------- sockClient -----------------------
var sockClient = {
    eventBus: null,
    endPointUrl: null,
    clientSubUrl_topicSpec: null,
    userId: null,
    userToken: null,
    reconnectTime: 0,
    init: function (userId, userToken, scoketUrl) {
        var that = this;
        that.userId = userId;
        that.userToken = userToken;
        that.endPointUrl = scoketUrl;
        that.clientSubUrl_topicSpec = 'clientSubUrl.topicSpec.' + userId;
    },
    connect: function () {
        var that = this;

        var toastOptions = {
            closeButton: true,
            debug: false,
            progressBar: true,
            preventDuplicates: false,
            positionClass: 'toast-bottom-right',
            onclick: function () {
                //window.location.href = window.rootPath + '/iWork/personal/center/3';
                location.hash = '#/messageCenter';
            },
            showDuration: '400',
            hideDuration: '1000',
            timeOut: '10000',
            extendedTimeOut: '10000',
            showEasing: 'swing',
            hideEasing: 'linear',
            showMethod: 'fadeIn',
            hideMethod: 'fadeOut'
        };

        var toastOptions2 = {
            closeButton: true,
            debug: false,
            progressBar: true,
            preventDuplicates: false,
            positionClass: 'toast-bottom-right',
            onclick: function () {
                //window.location.href = window.rootPath + '/iWork/org/addressBook/2';
                location.hash = '#/announcement';
            },
            showDuration: '400',
            hideDuration: '1000',
            timeOut: '10000',
            extendedTimeOut: '10000',
            showEasing: 'swing',
            hideEasing: 'linear',
            showMethod: 'fadeIn',
            hideMethod: 'fadeOut'
        };

        var updateCount = _.debounce(function () {
            var option = {};
            option.url = restApi.url_getMessageUnRead;
            option.classId = '';
            option.postData = {};
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    if (!isNullOrBlank(response.data) && response.data !== 0)
                        $('#unReadMessageCount').html(response.data);
                } else {
                    S_dialog.error(response.info);
                }
            });
        }, 1500);
        var updateCountByNotice = _.debounce(function () {
            var option = {};
            option.url = restApi.url_getNotReadNotice;
            option.classId = '';
            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    if (!isNullOrBlank(response.data) && response.data !== 0)
                        $('#unReadNoticeCount').html(response.data);
                } else {
                    S_dialog.error(response.info);
                }
            });
        }, 1500);

        that.eventBus = new EventBus(that.endPointUrl);
        that.eventBus.onopen = function () {
            var authHeader = {userId: that.userId, userToken: that.userToken};
            that.eventBus.registerHandler(that.clientSubUrl_topicSpec, authHeader, function (error, message) {
                if (message && message.body && !isNullOrBlank(message.body.content)) {
                    if (message.body.messageType === 'userMessage') {
                        updateCount();
                        toastr.info(message.body.content, null, toastOptions);
                    } else if (message.body.messageType === 'notice') {
                        updateCountByNotice();
                        toastr.info(message.body.content, null, toastOptions2);
                    }
                }
                /* 服务器端evenbus.public不能reply*/
                /* that.eventBus.send(message.replyAddress);*/
            });
        };
    },
    disconnect: function () {
        try {
            var root = sockClient;
            if (root.eventBus != null)
                root.eventBus.close();
        }
        catch (err) {
        }
    },
    sendAll: function (content) {
        var root = sockClient;
        var serverSubUrl_topicAll = 'serverSubUrl_topicAll';
        root.eventBus.publish(serverSubUrl_topicAll, {content: content});
    },
    sendSpec: function (userId, content) {
        var root = sockClient;
        var serverSubUrl_topicSpec = 'serverSubUrl_topicSpec/' + userId;
        root.eventBus.publish(serverSubUrl_topicSpec, {content: content});
    }
};

//-----------------------  messageCenter  -----------------------
var messageCenter = {
    init: function () {
        //  var id=root.randomId(100);
        //  var token='xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (a, b) {
        //      return b = Math.random() * 16, (a == 'y' ? b & 3 | 8 : b | 0).toString(16);
        //  });
        // /* console.log(id);
        //  console.log(token);*/
        //  $('#userId').val('dfg123456456');
        //  $('#userToken').val('654464');

        if (window.location.href.indexOf('/iWork/sys/workRegister') != -1)
            return;
        if (window.location.href.indexOf('/iWork/sys/login') != -1)
            return;

        var userId = window.currentUserId;
        var socketUrl = window.socketUrl;
        sockClient.init(userId, null, socketUrl);
        try {
            sockClient.connect();
        }
        catch (err) {
            console.error(err);
        }
    },
    randomId: function (max) {
        return parseInt(Math.random() * max + 1);
    },
    disconnect: function () {
        sockClient.disconnect();
    }
};
$(function () {
    messageCenter.init();
    window.onbeforeunload = function (event) {
        try {
            messageCenter.disconnect();
        }
        catch (e) {
        }
    };
});