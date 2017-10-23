//write by P9J7
//classId start from 38 to 100
for(var start = 38; start < 100; start++){
//fill 0
    String.prototype.lpad = function(padString, length) {
    var str = this;
    while (str.length <length)
    str = padString + str;
    return str;
    }
//select all choose
    var index = start.toString().lpad(0,10);
    var single = document.getElementsByName(index);
    if(single.length != 0){
    single[0].checked="checked";
    }

}
//remove waiting-time
EndTime = new Date();
document.forms['StDaForm'].submit();

