/**
 * Created by wrb on 2016/12/15.
 */

var home_createOrg={
    init:function(){
        var that=this;
        that.initCreateOrgData();

        var guide = setTimeout(function () {
            var orgEleLen = $('#navbar').find('.nav .orgInfo').length,orgEleHtml = $('#navbar').find('.nav .orgInfo').html();
            if(orgEleLen==0 || orgEleHtml==''){//没组织，提示帮助的引导
                var options = {};
                options.pageName = 'm_update_tips1';
                options.pageIndex = '1';
                $('body').m_update_tips(options);
            }
            clearTimeout(guide);
        },800);
    }
    ,initCreateOrgData:function () {
        var options = {};
        var currentCompanyId = window.currentCompanyId;
        options.saveOrgCallback = function (data) {
            var url = '/iWork/home/workbench';
            /*if (data != null && data != '')
                url = data;*/
            window.location.href = window.rootPath + url;
        };
        var isFirst = $('#isFirst').val();
        if(isFirst==='1'){
            options.showPre = $('#showPre').val();
            $('#createOrgBox').m_firstCreateOrg(options);
        }else{
            $('#createOrgBox').m_createOrg(options);
        }
    }
};
