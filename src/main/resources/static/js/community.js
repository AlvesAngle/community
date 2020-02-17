/*
* 自己定义的js 方法
* */

/*
* 提交post
* JSON.stringify() 方法用于将 JavaScript 值转换为 JSON 字符串。
* JSON.stringify(value[, replacer[, space]])
* */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1,content);

}
function comment2target(targetId, type,content) {
    if(!content){
        alert("不能回复空内容~~~");
        return;
    };
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if (response.code == 200){
                /*$("#comment_section").hide();*/
                window.location.reload();
            }else {
                if(response.code == 2003){
                    //windows 自带 弹窗, 是否跳转登录?
                    var isAccepted = confirm(response.message);
                    //点击确定
                    if(isAccepted){
                        window.localStorage.setItem("closable",true);
                        window.open("https://github.com/login/oauth/authorize?client_id=Iv1.0e169bb6b66ec2ad&redirect_uri=http://localhost:8887/callback&scope=user&state=1&");

                    }
                }else {
                    alert(response.message)
                }

            }

        },
        dataType: "json"
    });
}


function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId, 2,content);
}
/*
* 展开二级评论
* */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id); //$("#test") 获取id

    //获取二级评论展开状态
    var collapse = e.getAttribute("data-collapse");
    //判断 二级评论展开状态 是否为空
    if (collapse){
        //不为空 第二次点击
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else {
        //第一次点击

        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            //先异步请求获取 二级评论
            $.getJSON("/comment/" + id, function (data) {
                /*console.log(data.data.reverse());*/
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }

    }

}

function showSelectTag() {
    $("#select-tag").show();
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}

function show(e) {
    var commentId = e.getAttribute("data-id");
    console.log(commentId)
}