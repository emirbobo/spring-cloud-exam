System = {
    ExtNames : {
        img : ['jpg','png','jpeg','gif'],
        audio : ['mp3','wav','aac'],
        video : ['mp4','avi','mkv']
    }
    ,RegImg : null
    ,RegAudio : null
    ,RegVideo : null
    ,TypeConfig : []
    ,uploading : false
    ,markers : [] //已上传内容
    ,uploads : [] //已上传内容
    ,validBound : { swLat : 0,swLng : 0,neLat : 0,neLng : 0}
    ,minLat : -65
    , init: function (){
        this.RegImg = new RegExp('^.*\\.('+(this.ExtNames.img.join('|'))+')$','gi');
        this.RegAudio = new RegExp('^.*\\.('+(this.ExtNames.audio.join('|'))+')$','gi');
        this.RegVideo = new RegExp('^.*\\.('+(this.ExtNames.video.join('|'))+')$','gi');
        this.TypeConfig = [
            { key : 'img', reg : this.RegImg, type : '图片',max : 1024*1024*5 ,ext : this.ExtNames.img},
            { key : 'audio', reg : this.RegAudio, type : '音频',max : 1024*1024*15 ,ext : this.ExtNames.audio},
            { key : 'video', reg : this.RegVideo, type : '视频',max : 1024*1024*20  ,ext : this.ExtNames.video}
            ];
        $('#fileRule').html(this.getValidFileInfo('<br>'));
    }
    , getValidFileInfo : function (delimiter){
        var info = '';
        for(var i=0;i<this.TypeConfig.length;i++)
        {
            var t = this.TypeConfig[i];
            if(i > 0 && arguments.length>0)
                info+=delimiter;
            info += t.type+'文件('+ t.ext.join(',')+'),小于'+(t.max/1048576)+'M;';
        }
        return info;
    }
    , showDialog: function ( title) //
    {
        effects = ['blind', 'bounce', 'clip', 'drop', 'explode', 'fold', 'highlight', 'puff', 'pulsate', 'scale', 'shake', 'size', 'slide'];
        effect = 'highlight';//effects[self.effectInx];
        var width = 600;
        var buttons = [];
        var btnMap = [];
        buttons.push({
            text: "关闭",
            click: function () {
                $(this).dialog("close");
            }
        });
        var dialog = $("#dialog").dialog({
            autoOpen: false,
            title: title,
            width: width + 'px',
            modal: true,
            position: {
                my: "center",
                at: "center",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            }
            , buttons: buttons,
            close: function () {
                //alert('close dialog');
                //$("#dialogContent").html("");
            },
            hide: {
                effect: effect,
                duration: 500
            }
            ,beforeClose: function( event, ui ) {
                return System.checkCloseDialog();
            }
        });
        dialog.dialog("open");
        if(arguments.length>0) $("#dialogTitle").html(title);
        //$("#dialogContent").html(html);
        //$('.ui-widget-overlay').bind("click",function(){
        //    dialog.dialog( "close" );
        //});
    }
    ,checkCloseDialog:function(){
        if( this.uploading )
        {
            alert("请等待上传完成");
            return false;
        }
        return true;
    }
    ,reloadUsers:function(map){
        var self = this;
        for(var i in self.markers)
        {
            var marker = self.markers[i];
            marker.setMap(null);
        }
        self.markers = [];
        self.uploads = [];
        $.ajax(
            {
                url : 'uploads.txt?ver='+Math.random()
                ,type : 'GET'
                ,dataType : 'json'
                ,success : function(resp)
            {
                if(resp.length>0)
                {
                    //alert('已经有'+resp.length+"用户上传");
                    for(var i=0;i<resp.length;i++)
                    {
                        var u = resp[i];
                        System.uploads.push(u);
                        var latLng = new google.maps.LatLng(u.posLat, u.posLng);
                        var marker = new google.maps.Marker({
                            position: latLng,
                            title : '上传者：'+ u.user+" ,点击显示上传内容",
                            map: map
                        });
                        var clickFunc = (function(e){
                            var data = System.uploads[arguments.callee.i];
                            var html = '<ul class="list-group">';
                            html+='<li class="list-group-item">由 <label class="label label-primary">'+data.user+'</label> 上传</li>';
                            if(data.img.length > 0)
                                html+='<li class="list-group-item"><img onload="System.autoAdjustImg(this,200)" src="upload/'+data.img+'"></li>';
                            if(data.audio.length > 0)
                                html+='<li class="list-group-item"><audio src="upload/'+data.audio+'" controls></audio></li>';
                            if(data.video.length > 0)
                                html+='<li class="list-group-item"><video oncanplay="System.autoAdjustVideo(this,200)" height="320" src="upload/'+data.video+'" controls></video></li>';
                            var infowindow = new google.maps.InfoWindow({
                                content: html,
                                position: new google.maps.LatLng(data.posLat, data.posLng)
                            });
                            html+='</ul>';
                            infowindow.open(MD.map);
                        });
                        clickFunc.i = i;
                        marker.addListener('click',clickFunc);
                        self.markers.push(marker);
                    }
                }
            }
                ,error:function(o,err,exc){}
            }
        );
    }
    , statFiles : function (fileInputArr)
    {
        var rst = { img : 0 , audio : 0,video : 0};
        for(var i =0 ; fileInputArr,i<fileInputArr.length;i++)
        {
            var fi = fileInputArr[i];
            if(fi.files.length > 0)
            {
                var file = fi.files[0];
                if(file.name.match(this.RegImg))
                {
                    rst.img++;
                    continue;
                }
                if(file.name.match(this.RegAudio))
                {
                    rst.audio++;
                    continue;
                }
                if(file.name.match(this.RegVideo))
                {
                    rst.video++;
                    continue;
                }
            }
        }
        return rst;
    }
    , previewFile: function (fileInput)
    {
        var self = this;
        var reader = new FileReader();
        var infoDiv = $(fileInput.parentNode).find('.file-info')[0];
        if(fileInput.files.length == 0)
        {
            $(infoDiv).html('');
            return;
        }
        var file = fileInput.files[0];
        var img = false;
        var name = file.name.toLowerCase();
        var i = 0;
        for(i =0;i<System.TypeConfig.length;i++) // 检测文件大小
        {
            var typeInfo = System.TypeConfig[i];
            if(name.match(typeInfo.reg))
            {
                if( file.size > typeInfo.max )
                {
                    alert(typeInfo.type+'文件不能大于'+(typeInfo.max/1048576)+'M');
                    System.cancelFile(null,fileInput);
                    return;
                }
                $(infoDiv).html(typeInfo.type+'文件,');
                if(typeInfo.key == 'img')
                    img = true;
                break;
            }
        }
        if(i >= System.TypeConfig.length)
        {
            System.cancelFile(null,fileInput);
            alert('不能上传该文件：'+name+"\n\n上传文件规格如下：\n\n"+self.getValidFileInfo('\n'));
            return;
        }
        reader.onerror = function (e) {
        };
        reader.onload = function (e) {
            //alert('onload '+e);
            var infoHtml = $(infoDiv).html();
            if(infoHtml.length > 0)
            {
                infoHtml+='大小:'+file.size;
                infoHtml='<h5><span class="label label-success">'+infoHtml+'</span></h5>';
                if(img)
                {
                    infoHtml+=' <img>';
                }
                infoHtml+='&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-xs btn-warning" onclick="System.cancelFile(this)">取消</button>';
                $(infoDiv).html(infoHtml);
            }
            if(img)
            {
                img = $(infoDiv).find('img')[0];
                img.src = this.result;
                img.onload = System.autoAdjustImg;
            }
        };
        reader.readAsDataURL(file);
    }
    , cancelFile : function(btn,fi)
    {
        if(arguments.length < 2)
            fi = $(btn.parentNode.parentNode).find('input[type=file]')[0];
        $(fi).val('');
        fi.onchange.call(fi);
        return true;
    }
    , autoAdjustImg : function(e)
    {
        var img = ( e instanceof HTMLImageElement) ? e : e.target;
        var maxSize = arguments.length > 1 ? arguments[1] : 100.0;
        var scale =  img.width / maxSize > img.height / maxSize ? img.width / maxSize : img.height / maxSize;
        $(img).css('width',Math.round(img.width / scale )+'px');
    }
    , autoAdjustVideo : function(e)
    {
        //var video = ( e instanceof HTMLVideoElement) ? e : e.target;
        //alert(''+e+' '+e.height);
        //alert(video.height);
        //var maxSize = arguments.length > 1 ? arguments[1] : 100.0;
        //var scale =  img.width / maxSize > img.height / maxSize ? img.width / maxSize : img.height / maxSize;
        //$(img).css('width',Math.round(img.width / scale )+'px');
    }
    , handleMapChanged : function(e,map)
    {
        var bounds = map.getBounds();
        var sw = bounds.getSouthWest();
        if(sw.lat() < -65 )
        {
            map.fitBounds(
                new google.maps.LatLngBounds(
                    new google.maps.LatLng({lat: System.validBound.swLat, lng: System.validBound.swLng}),
                    new google.maps.LatLng({lat: System.validBound.neLat, lng: System.validBound.neLng})
                )
            );
        }
        //console.log();
    }
    , handleMapBoundsChanged : function(e,map)
    {
        var bounds = map.getBounds();
        var sw = bounds.getSouthWest();
        var ne = bounds.getNorthEast();
        if(sw.lat() > System.minLat )
        {
            System.validBound.swLat = sw.lat();
            System.validBound.swLng = sw.lng();
            System.validBound.neLat = ne.lat();
            System.validBound.neLng = ne.lng();
        }
        //console.log("zoom : "+map.getZoom())
    }
    ,log : function()
    {

    }
};
System.init();
/*
 function handleFileSelect(evt) {
 var files = evt.target.files; // FileList object

 // files is a FileList of File objects. List some properties.
 var output = [];
 for (var i = 0, f; f = files[i]; i++) {
 output.push('<li><strong>', escape(f.name), '</strong> (', f.type || 'n/a', ') - ',
 f.size, ' bytes, last modified: ',
 f.lastModifiedDate ? f.lastModifiedDate.toLocaleDateString() : 'n/a',
 '</li>');
 }
 document.getElementById('list').innerHTML = '<ul>' + output.join('') + '</ul>';
 }

 document.getElementById('files').addEventListener('change', handleFileSelect, false);

 */