<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv=Content-Type content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
    <link rel="stylesheet" href="/css/d.css">
    <link rel="stylesheet" href="/css/swiper.min.css">
    <title>数字尾巴</title>
</head>
<body>
<app></app>

<script type="text/javascript" src="<@spring.url ''/>/js/lib/X.js"></script>
<script type="text/javascript" src="<@spring.url ''/>/js/lib/fastclick.js"></script>
<script type="text/javascript" src="<@spring.url ''/>/js/lib/jquery.min.js"></script>
<script type="text/javascript" src="<@spring.url ''/>/js/lib/templet.js"></script>
<script type="text/javascript" src="<@spring.url ''/>/js/lib/swiper.jquery.min.js"></script>
<script type="text/javascript">
    var addr = '<@spring.url ''/>';
    if ('addEventListener' in document) {
        document.addEventListener('DOMContentLoaded', function() {
            FastClick.attach(document.body);
        }, false);
    }

    fmtDate = function(date,ignore){
        var months = [
            "January", "February", "March",
            "April", "May", "June", "July",
            "August", "September", "October",
            "November", "December"
        ];

        var now = new Date(Number(date)*1000),
                _d = now.getDate(),
                m_index = now.getMonth(),
                _y = now.getFullYear(),
                zero = _d<10?"0"+_d:_d;

        if (ignore) {
            return months[m_index]+' '+zero;
        }
        return months[m_index]+' '+zero+', '+_y;
    }


    /* 加载更多 */
    $('body').on('click', '.load-more', function () {
        var $btn = $(this),number = $btn.attr('d-number'),
                isLast = $btn.attr('d-isLast');
        xhr(addr+'/dgtle/getArticles/10/'+ number+1, function (data) {
            var template = $('#itemTpl').html();
            var rendered = _.render(template, data);

            /*var tml ='';
            for(var i = 0; i< data.articles.length; i++){
                tml +=  '<li class="card"><a href="#!article/'+data.articles[i].id+'">'+
                        '<img class="cover" src="'+data.articles[i].pic_url+'"><div class="title"><span>'+data.articles[i].title+'</span></div>'+
                        '<div class="info"><p>'+data.articles[i].summary+'</p></div></a></li>'
            }*/

            $(rendered).appendTo($('ul'));
            $btn.attr('d-number', data.number).attr('d-isLast', data.isLast);
            if (data.isLast) {
                $('.more-box').remove();
            }
        });
    })

    /* ajax get */
    var xhr = function (url, cb) {
        $.get(url, function (data) {
            cb(data);
        }).fail(function (XMLHttpRequest, textStatus) {
            if(textStatus=="timeout")
                alert('请求超时,请稍后重试。');
            else
                alert(textStatus+' 网络连接中断 :(');
        });
    }

    /* 返回顶部 */
    $('body').on('click', '.efo', function () {
        $(window).scrollTop(0);
    });


    X.get('home', function () {
        console.log('你打开了首页');
        xhr(addr+'/dgtle/homeTpl/', function (tpl) {
            $('app').html(tpl);
        });

    }).get('article', function (id) {
        if (!id)
            return alert('传入的文章id不正确！');
        xhr(addr+'/dgtle/getArticle/'+ id, function (data) {
            $('app').empty();
            var template = $('#articleTpl').html();
            var rendered = _.render(template, data);
            $(rendered).appendTo($('app'));
            $('app').find('img').each(function () {
                $(this).attr('src', $(this).parent().attr('href')).attr('width','100%')
            });
            $(window).scrollTop(0);
        })

    });

    /* 初始化 */
    X.init({
        prefix:'!',
        index:'home',
        pop: function (route) {
            console.log('当前route:'+route)
        }
    });

</script>

<!-- #templte -->
<xmp id="itemTpl">
    {{#articles}}
    <li class="card">
        <a href="#!article/{{id}}">
            <img class="cover" src="{{pic_url}}">
            <div class="title">
                <span>{{title}}</span>
            </div>
            <div class="info">
                <p>{{summary}}</p>
            </div>
        </a>
    </li>
    {{/articles}}
</xmp>

<xmp id="articleTpl">
    <article>
    <#--http://www.dgtle.com/uc_server/avatar.php?uid=90433&size=middle -->
        <div class="title"><h2>{{title}}</h2> <p>{{dateline|fmtDate}}</p> </div>
        <div class="body">{{&html}}</div>
        <div class="efo"><hr><p>返回顶部</p></div>
    </article>
</xmp>
<!-- /templte -->
</body>
</html>