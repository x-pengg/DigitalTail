<div class="swiper-container">
    <div class="swiper-wrapper">
        <#list hot as _>
            <div class="swiper-slide" href="#!article/${_.id}" style="background-image:url(${_.pic_url})"></div>
        </#list>
    </div>
    <div class="swiper-pagination"></div>
</div>
<script>
    /* 这家伙太重了 */
    $(document).ready(function () {
        var mySwiper = new Swiper ('.swiper-container', {
            pagination: '.swiper-pagination',
            paginationClickable: true,
            loop: true,
            autoplay: 2000
        })
    });
</script>
<ul>
<#list data.articles as article>
    <li class="card">
        <a href="#!article/${article.id}">
            <img class="cover" src="${article.pic_url}">
            <div class="title">
                <span>${article.title}</span>
            </div>
            <div class="info">
                <p>${article.summary}</p>
            </div>
        </a>
    </li>
</#list>
</ul>
<#if !data.isLast>
<div class="more-box">
    <button class="load-more" d-number="${data.number}" d-isLast="${data.isLast?c}">加載更多</button>
</div>
</#if>