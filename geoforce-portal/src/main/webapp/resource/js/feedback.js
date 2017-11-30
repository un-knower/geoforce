/**
 * Created by zr on 2018/1/31.
 */
$(function(){
    var imgNum = 3;
    var delParent;
    var defaults = {
        fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
        // fileSize         : 1024 * 1024 * 10                  // 上传文件的大小 10M
        fileSize         : 1024 * 1024                // 上传文件的大小 1M
    };
    /*点击图片的文本框*/
    $(".file").change(function(){
        var idFile = $(this).attr("id");
        var file = document.getElementById(idFile);
        var imgContainer = $(this).parents(".img-box"); //存放图片的父亲元素
        var fileList = file.files; //获取的图片文件
        // console.log(fileList+"======filelist=====");
        var input = $(this).parent();//文本框的父亲元素
        var imgArr = [];
        //遍历得到的图片文件
        var numUp = imgContainer.find(".up-section").length;
        var totalNum = numUp + fileList.length;  //总的数量
        if(fileList.length > imgNum || totalNum > imgNum ){
            alert("上传图片数目不可以超过3个，请重新选择");  //一次选择上传超过5个 或者是已经上传和这次上传的到的总数也不可以超过5个
        }
        else if(numUp < imgNum){
            fileList = validateUp(fileList);
            for(var i = 0;i<fileList.length;i++){
                var imgUrl = window.URL.createObjectURL(fileList[i]);
                imgArr.push(imgUrl);
                var $section = $("<div class='img-son-box fl loading'>");
                imgContainer.prepend($section);
                // var $span = $("<span class='up-span'>");
                // $span.appendTo($section);

                var $img0 = $("<img class='close-upimg'>").on("click",function(event){
                    event.preventDefault();
                    event.stopPropagation();
                    $(".works-mask").show();
                    delParent = $(this).parent();
                });
                $img0.attr("src","resource/img/a7.png").appendTo($section);
                var $img = $("<img class='up-img up-opcity'>");
                $img.attr("src",imgArr[i]);
                $img.appendTo($section);
                // var $p = $("<p class='img-name-p'>");
                // $p.html(fileList[i].name).appendTo($section);
                var $input = $("<input id='taglocation' name='taglocation' value='' type='hidden'>");
                $input.appendTo($section);
                var $input2 = $("<input id='tags' name='tags' value='' type='hidden'/>");
                $input2.appendTo($section);

            }
        }
        setTimeout(function(){
            $(".img-son-box").removeClass("loading");
            $(".up-img").removeClass("up-opcity");
        },450);
        numUp = imgContainer.find(".img-son-box").length;
        if(numUp >= imgNum){
            $(this).parent().hide();
        }

        //input内容清空
        $(this).val("");
    });



    $(".z_photo").delegate(".close-upimg","click",function(){
        $(".works-mask").show();
        delParent = $(this).parent();
    });

    $(".wsdel-ok").click(function(){
        $(".works-mask").hide();
        var numUp = delParent.siblings().length;
        if(numUp < 6){
            delParent.parent().find(".z_file").show();
        }
        delParent.remove();

    });

    $(".wsdel-no").click(function(){
        $(".works-mask").hide();
    });

    function validateUp(files){
        var arrFiles = [];//替换的文件数组
        for(var i = 0, file; file = files[i]; i++){
            //获取文件上传的后缀名
            var newStr = file.name.split("").reverse().join("");
            if(newStr.split(".")[0] != null){
                var type = newStr.split(".")[0].split("").reverse().join("");
                // console.log(type+"===type===");
                if(jQuery.inArray(type, defaults.fileType) > -1){
                    // 类型符合，可以上传
                    if (file.size >= defaults.fileSize) {
                        alert(file.size);
                        alert('您这个"'+ file.name +'"文件大小过大');
                    } else {
                        // 在这里需要判断当前所有文件中
                        arrFiles.push(file);
                    }
                }else{
                    alert('您这个"'+ file.name +'"上传类型不符合');
                }
            }else{
                alert('您这个"'+ file.name +'"没有类型, 无法识别');
            }
        }
        return arrFiles;
    }

    //提交操作
    $("#save").click(function(){
        if ($("#advance").val().length == 0){
            $("#cue1").hide();
            $("#cue2").show();
        }else if($("#tel").val().length != 0 && $("#tel").val().length < 11){
            $("#cue3").show();
        }else{
            alert("保存成功~");
        }
    });

    $("#tel").keyup(function(){
        if($(this).val().length == 11){
            $("#cue3").hide();
        }
    });
    $("#advance").keyup(function(){
        var a = $(this).val().length;
        if(a>0){
            $("#cue1").show();
            $("#cue2").hide();
        }
        // console.log(a);
        $("#check-num").html(a);
    });

});
