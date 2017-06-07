//write by P9J7
for(var start = 38; start < 100; start++){

    String.prototype.lpad = function(padString, length) {
    var str = this;
    while (str.length <length)
    str = padString + str;
    return str;
    }

    var index = start.toString().lpad(0,10);
    var single = document.getElementsByName(index);
    if(single.length != 0){
    single[0].checked="checked";
    }

}
EndTime = new Date();
document.forms['StDaForm'].submit();

