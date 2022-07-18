$(document).ready(function(){
    $("#getLotto").on("click", function(){
       var size = $("#size").val();
       if(size == undefined || size == 0){
           alert('추천받을 로또 횟수를 입력해주세요');
           return;
       }
       getLotto(size);
    });
});

function getLotto(size){
    $.ajax({
        url: "/lotto/recommend?size="+size,
        data: {},
        type: "GET",
        dataType: "json",
        success: function (data) {
            var html = '';
            $('#lottoList').html('');
            for(var i=0; i<data.length; i++){
                html += '        <tr>\n';
                html += '            <td>'+(i+1)+'</td>\n';
                html += '            <td>'+data[i].num1+'</td>\n';
                html += '            <td>'+data[i].num2+'</td>\n';
                html += '            <td>'+data[i].num3+'</td>\n';
                html += '            <td>'+data[i].num4+'</td>\n';
                html += '            <td>'+data[i].num5+'</td>\n';
                html += '            <td>'+data[i].num6+'</td>\n';
                html += '        </tr>\n';
            }
            $('#lottoList').html(html);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}