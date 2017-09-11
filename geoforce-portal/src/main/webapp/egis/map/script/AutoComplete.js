/**
    author ：闵益飞
    date   ：2011-3-20
    demo   :
            autoComplete = new MyfAutoComplete("txtName"); //txtName为显示的文本框ID
            autoComplete.bold = false;
            autoComplete.afterEvent = function(key){alert(key)};
            var arrData = [{ value: "100100", text: '北京超图' }, '南京超图', '西安超图'];
            autoComplete.show(arrData);
**/

var MyfAutoComplete = function(inputId, elemCss) {
    /**
    * inputId 需要绑定的文本框,必填
    * elemCSS 样式参数,可选参数
    */
    this.init(inputId, elemCss);
}

MyfAutoComplete.prototype = {
    //输入框
    input: null,
    //弹出框
    popup: null,
    //样式
    elemCss: null,
    //数据
    data: null,
    //是否已经显示
    using: 0,
    //a的访问游标
    current: -1,
    //关键字是否加粗显示,默认加粗
    keyBold: true,
    //选中事件后激发事件
    afterEvent: function(key) { },
    //初始化控件
    init: function(inputId, elemCss) {
        var that = this;
        //输入框
        this.input = document.getElementById(inputId);
        //弹出框
		this.popup = document.createElement("div");
		// if(!document.getElementById("autoComp")){
			// this.popup = document.createElement("div");
			// this.popup.id = "autoComp";
		// }else {
			// var div = document.getElementById("autoComp");
			// div.parentNode.removeChild(div);
			// this.popup = document.createElement("div");
			// this.popup.id = "autoComp";
		// }
        //样式
        if (typeof (elemCss) != "undefined") {
            this.elemCss = elemCss;
        } else {
            this.elemCss = {
                focus: { 'color': 'black', 'background': '#ccc' ,padding:"5px"},
                blur: { 'color': '#6B6B6B', 'background': '#fff' },
                popup: { border: '1px solid #A9B7D1', borderWidth: 1, background: "#fff",clear:"both" }
            }
        }
        //数据
        this.data = null;
        document.body.appendChild(this.popup);
        //获取文本框的位置
        var pos = this.getElementPostion(this.input);
        var popupStyle = that.popup.style;
        popupStyle.left = pos.x + 'px';
        popupStyle.top = pos.y - 0 + that.input.offsetHeight + 'px';
        popupStyle.width = that.input.offsetWidth - that.elemCss.popup.borderWidth + 'px';
        popupStyle.border = that.elemCss.popup.border;
        popupStyle.zIndex = 99999;
        popupStyle.display = 'none';
        popupStyle.position = 'absolute';
        popupStyle.background = that.elemCss.popup.background;
        popupStyle.textAlign = 'left';
        popupStyle.clear = that.elemCss.popup.clear;
    },
    //初始化各种事件
    initEvent: function() {
        var that = this;
        //文本框获得焦点事件
        this.input.onfocus = function() {
			if( that == null || that.input == null ){
				return;
			}
			if(that.input.value.length == 0){
				that.popup.style.display = 'none';
			}
			
            var els = that.popup.getElementsByTagName('a');
            if (els.length > 0 && that.input.value.length > 0) {
                that.popup.style.display = 'block';
            }
        };
        //文本框失去焦点后
        this.input.onblur = function() {
			if(that == null || that.popup == null){
				return;
			}
            //焦点游标
            var current = that.current;
            var els = that.popup.getElementsByTagName('a');
            var len = els.length - 1;
            //li-a-onclick
            var aClick = function() {
                that.input.value = this.title;
                that.popup.innerHTML = '';
                that.popup.style.display = 'none';
                that.input.focus();
                that.using = 0;
                that.afterEvent(this.title);
            };

            //li-a-focus
            var aFocus = function() {
                for (var i = len; i >= 0; i--) {
                    if (this.parentNode === that.popup.children[i]) {
                        current = i;
                        break;
                    }
                }
                for (var k in that.elemCss.focus) {
                    this.style[k] = that.elemCss.focus[k];
                }
            };
            //li-a-blur
            var aBlur = function() {
                for (var k in that.elemCss.blur) {
                    this.style[k] = that.elemCss.blur[k];
                }
            };
            //li-a-keydown
            var aKeydown = function(event) {
                event = event || window.event;
                if (current === len && event.keyCode === 9) {
                    //当游标到最后一个时，继续按tab键将隐藏下拉框
                    that.popup.style.display = 'none';
                } else if (event.keyCode == 40) {
                    //Down
                    current++;
                    if (current < -1) current = len;
                    if (current > len) {
                        current = -1;
                        that.input.focus();
                    } else {
                        that.popup.getElementsByTagName('a')[current].focus();
                    }
                    that.stopDefaultEvent(event);
                } else if (event.keyCode == 38) {
                    //Up
                    current--;
                    if (current == -1) {
                        that.input.focus();
                    } else if (current < -1) {
                        current = len;
                        that.popup.getElementsByTagName('a')[current].focus();
                    } else {
                        that.popup.getElementsByTagName('a')[current].focus();
                    }
                    that.stopDefaultEvent(event);
                }
                
            };

            //为每个li-a绑定事件
            for (var i = 0; i < els.length; i++) {
                els[i].onclick = aClick;
                els[i].onfocus = aFocus;
                els[i].onblur = aBlur;
                els[i].onkeydown = aKeydown;
            }
        };
        //文本框keydown事件
        this.input.onkeydown = function(event) {
			if(that == null || that.popup == null){
				return;
			}
			
            event = event || window.event;
            var els = that.popup.getElementsByTagName('a');
            if (event.keyCode == 40) {
                //Down
                if (els[0]) {
                    els[0].focus();
                    that.current = 0;
                }
                that.stopDefaultEvent(event); 
            } else if (event.keyCode == 38) {
                //Up
                if (els[els.length - 1]) {
                    els[els.length - 1].focus();
                }
                that.stopDefaultEvent(event);
            } else if (event.keyCode == 9) {
                //Tab
                if (event.shiftKey == true) {
                    that.popup.style.display = 'none';
                }
            } 
			// else if(event.keyCode == 13 || event.keyCode == 8 || event.keyCode == 46){
				// if( that.input.value.length == 0 ){
					// that.popup.style.display = 'none';
				// }
			// }
           
        };

        this.input.onmouseover = function() {
			that.using = 1;
        }
        this.input.onmouseout = function() {
            that.using = 0;
        }
        this.popup.onmouseover = function() {
            that.using = 1;
        }
        this.popup.onmouseout = function() {
            that.using = 0;
        }

        if (document.addEventListener) {
            document.addEventListener('click', function() {
                if (that && that.popup && that.using == 0) {
                    that.popup.style.display = 'none';
                }
            }, false);

        } else if (document.attachEvent) {
            document.attachEvent('onclick', function() {
                if (that.using == 0) {
                    that.popup.style.display = 'none';
                }
            });
        }

    },
    stopDefaultEvent: function(event) {
        if (event.preventDefault)
            event.preventDefault();
        //IE中阻止函数器默认动作的方式
        else
            window.event.returnValue = false;
        return false;
    },
    //设置显示数据 Array(String) || Array<value,text>，第一个string为填入文本框的值，第二个为显示值
    setData: function(data) {
        this.data = [];
        if (typeof (data) != 'undefined' && typeof (data) == "object") {
            var info = null;
            for (var i = 0; i < data.length; i++) {
                if (!data[i].value) {
                    info = { value: data[i], text: data[i] };
                    this.data.push(info);
                } else {
                    info = { value: data[i].value, text: data[i].text };
                    this.data.push(info);
                }
            }
        }
    },
    hide: function() {
		if(this.popup){
			this.popup.style.display = 'none';
		}
    },
    show: function(data) {
        var that = this;
        if (data) {
            this.setData(data);
        }
        //清空下拉框的内容
        this.popup.innerHTML = "";
        var inputValue = this.input.value.toString();
        var tagA = null;
        var aText = null;
        //遍历数组，生成li
        for (var i in this.data) {
            if (inputValue.length > 0) {
                tagA = document.createElement('a');
                if (that.keyBold) {
                    aText = this.data[i].text.toString().replace(inputValue, '<b>' + inputValue + '</b>');
                } else {
                    aText = this.data[i].text.toString();
                }
                tagA.title = this.data[i].value.toString();
				//判断tagA.title的值是不是和aText的值样，如果样的话。就只显示一次
				if(tagA.title.replace(inputValue, '<b>' + inputValue + '</b>') == aText){
					tagA.innerHTML = aText;
				}else{
					tagA.innerHTML = tagA.title + aText;
				}
                tagA.href = 'javascript:;';
                tagA.style.display = 'block';
                tagA.style.textDecoration = 'none';
                tagA.style.padding = '1px 0 1px 0';
                tagA.style.color = that.elemCss.blur.color;
                tagA.style.heigth = '25px';
                tagA.onmouseout = function() {
                    this.style.background = that.elemCss.blur.background;
                    this.style.color = that.elemCss.blur.color;
                }
                tagA.onmouseover = function() {
                    this.style.background = that.elemCss.focus.background;
                    this.style.color = that.elemCss.focus.color;
                }
                this.popup.appendChild(tagA);
            }
        }

        if (this.popup.getElementsByTagName('a').length) {
            this.popup.style.display = 'block';
        } else {
            this.popup.style.display = 'none';
        }
        this.initEvent();
    },
    distroy: function() {
        document.body.removeChild(this.popup);
        this.popup = null;
        this.data = null;
        this.input = null;
        this.elemCss = null;
    },
    //事件
    addEvent: function(element, type, fn) {
        if (document.addEventListener) {
            element.addEventListener(type, fn, false);
        } else if (document.attachEvent) {
            element.attachEvent('on' + type, fn);
        }
    },
    /*计算控件的位置
    * elelment 需要计算控件
    * tagName 计算相对于tagName的位置，默认tagName为空计算相对于整个页面的位置
    * return {x:Number,y:Number}
    */
    getElementPostion: function(element, tagName, position) {
        var that = this;
        if (typeof (position) == "undefined" || position == null) {
            position = { x: 0, y: 0 };
        }

        if (element.offsetParent == null || element.offsetParent.tagName.toLowerCase() == tagName) {
            // 结束递归调用
            if (typeof (element.style.borderLeftWidth) != "unknown" && element.style.borderLeftWidth != null && element.style.borderLeftWidth != "") {
                position.x += eval(element.offsetLeft) + eval(element.style.borderLeftWidth.replace("px", ""));
                position.y += eval(element.offsetTop) + eval(element.style.borderTopWidth.replace("px", ""));
                return position;
            }
            else {
                position.x += eval(element.offsetLeft);
                position.y += eval(element.offsetTop);
            }
            return position;
        } else {
            if (typeof (element.style.borderLeftWidth) != "unknown" && element.style.borderLeftWidth != null && element.style.borderLeftWidth != "") {
                // 累加边框的大小
                position.x += eval(element.offsetLeft) + eval(element.style.borderLeftWidth.replace("px", ""));
                position.y += eval(element.offsetTop) + eval(element.style.borderTopWidth.replace("px", ""));
                return position;
            }
            else {
                // 累加相对于父一级元素偏移量
                position.x += eval(element.offsetLeft);
                position.y += eval(element.offsetTop);
            }
            // 递归到父一级元素继续进行累加
            return that.getElementPostion(element.offsetParent, tagName, position);
        }
    }
}

