(function(SuperMap) {
	//panguso.maps.event.addListener(map, 'contextmenu', function() {
	//	alert('map rightclick');
	//});
	var timeCounter = 0;
	var j$ = jQuery;
	/*****************************Begin：jQuery右键菜单插件***************************************************/
	/**右键菜单类会调用封装好的这段代码**/
	var contextMenuEnabled = true;
	if(jQuery)( function() {
//		var $ = j$;
		j$.extend($.fn, {
			contextMenu: function(o, callback) {
				
				// Defaults
				if( o.menu == undefined ) return false;
				if( o.inSpeed == undefined ) o.inSpeed = 150;
				if( o.outSpeed == undefined ) o.outSpeed = 75;
				// 0 needs to be -1 for expected results (no fade)
				if( o.inSpeed == 0 ) o.inSpeed = -1;
				if( o.outSpeed == 0 ) o.outSpeed = -1;
				// Loop each context menu
				$(this).each( function() {
						var el = $(this);
						// Add contextMenu class
						$('#' + o.menu).addClass('contextMenu');
						// Simulate a true right click
						$(this).mousedown( function(e) {
							var evt = e;
//							evt.stopPropagation();
							$(this).mouseup( function(e) {
								var date = new Date();
								timeCounter = date.getTime();
								date = null;
								//单击右键300毫秒后弹出右键菜单
								var timer = setTimeout(function(){
									showMenu();
									timer = null;
								},200);
								function showMenu(){
									//若两次单击的时间间隔少于190毫秒，则前一次单击不弹出右键菜单
									var date = new Date();
									if(date.getTime()-timeCounter<190){
										date = null;
										return;
									}
//									e.stopPropagation();
									var srcElement = $(this);
									var evt=e;
									$(this).unbind('mouseup');
									if( evt.button == 2 ) {
										// Hide context menus that may be showing
										$(".contextMenu").hide();
										// Get this context menu
										var menu = $('#' + o.menu);
										//if( $(el).hasClass('disabled') ) return false;
										if(contextMenuEnabled == false) return false;
										// Detect mouse position
										var d = {}, x, y,h=216,w=152;
										if( self.innerHeight ) {
												d.pageYOffset = self.pageYOffset;
												d.pageXOffset = self.pageXOffset;
												d.innerHeight = self.innerHeight;
												d.innerWidth = self.innerWidth;
										} else if( document.documentElement && document.documentElement.clientHeight ) {
											d.pageYOffset = document.documentElement.scrollTop;
											d.pageXOffset = document.documentElement.scrollLeft;
											d.innerHeight = document.documentElement.clientHeight;
											d.innerWidth = document.documentElement.clientWidth;
										} else if( document.body ) {
											d.pageYOffset = document.body.scrollTop;
											d.pageXOffset = document.body.scrollLeft;
											d.innerHeight = document.body.clientHeight;
											d.innerWidth = document.body.clientWidth;
										}
										
										(e.pageX) ? x = e.pageX : x = e.clientX + d.scrollLeft;
										(e.pageY) ? y = e.pageY : y = e.clientY + d.scrollTop;
										
										var left = (d.innerWidth-x<w)?x-w:x;
										var top = (d.innerHeight-y<h)?y-h:y;
										
										// Show the menu
										$(document).unbind('click');
										$(menu).css({ top: top, left: left }).fadeIn(o.inSpeed);
										// Hover events
										$(menu).find('A').mouseover( function() {
											$(menu).find('LI.hover').removeClass('hover');
											$(this).parent().addClass('hover');
										}).mouseout( function() {
											$(menu).find('LI.hover').removeClass('hover');
										});
										// Keyboard
										$(document).keypress( function(e) {
											switch( e.keyCode ) {
											case 38: // up
												if( $(menu).find('LI.hover').size() == 0 ) {
													$(menu).find('LI:last').addClass('hover');
												} else {
													$(menu).find('LI.hover').removeClass('hover').prevAll('LI:not(.disabled)').eq(0).addClass('hover');
													if( $(menu).find('LI.hover').size() == 0 ) $(menu).find('LI:last').addClass('hover');
												}
												break;
											case 40: // down
												if( $(menu).find('LI.hover').size() == 0 ) {
													$(menu).find('LI:first').addClass('hover');
												} else {
													$(menu).find('LI.hover').removeClass('hover').nextAll('LI:not(.disabled)').eq(0).addClass('hover');
													if( $(menu).find('LI.hover').size() == 0 ) $(menu).find('LI:first').addClass('hover');
												}
												break;
											case 13: // enter
												$(menu).find('LI.hover A').trigger('click');
												break;
											case 27: // esc
												$(document).trigger('click');
												break;
											}
										});
										// When items are selected
										$('#' + o.menu).find('A').unbind('click');
										$('#' + o.menu).find('LI:not(.disabled) A').click( function() {
											$(document).unbind('click').unbind('keypress');
											$(".contextMenu").hide();
											// Callback
											if( callback ){
												var offset = $(el).offset();
												callback( $(this).attr('id').substr(1), $(srcElement), {x: x - offset.left, y: y - offset.top, docX: x, docY: y} );
											}
											return false;
										});
										// Hide bindings
										setTimeout( function() { // Delay for Mozilla
											$(document).click( function() {
												$(document).unbind('click').unbind('keypress');
												$(menu).fadeOut(o.outSpeed);
												return false;
											});
										}, 0);
									}
								}
							});
						});
						// Disable text selection
						if( $.browser.mozilla ) {
							$('#' + o.menu).each( function() { $(this).css({ 'MozUserSelect' : 'none' }); });
						} else if( $.browser.msie ) {
							$('#' + o.menu).each( function() { $(this).bind('selectstart.disableTextSelect', function() { return false; }); });
						} else {
							$('#' + o.menu).each(function() { $(this).bind('mousedown.disableTextSelect', function() { return false; }); });
						}
						// Disable browser context menu (requires both selectors to work in IE/Safari + FF/Chrome)
						$(el).add($('UL.contextMenu')).bind('contextmenu', function() { return false; });
				});
				return $(this);
			},
			// Disable context menu items on the fly
			disableContextMenuItems: function(o) {
				if( o == undefined ) {
					// Disable all
					$(this).find('LI').addClass('disabled');
					return( $(this) );
				}
				$(this).each( function() {
					if( o != undefined ) {
						var d = o.split(',');
						for( var i = 0; i < d.length; i++ ) {
							$(this).find('A[href="' + d[i] + '"]').parent().addClass('disabled');
						}
					}
				});
				return( $(this) );
			},
			// Enable context menu items on the fly
			enableContextMenuItems: function(o) {
				if( o == undefined ) {
					// Enable all
					$(this).find('LI.disabled').removeClass('disabled');
					return( $(this) );
				}
				$(this).each( function() {
					if( o != undefined ) {
						var d = o.split(',');
						for( var i = 0; i < d.length; i++ ) {
							$(this).find('A[href="' + d[i] + '"]').parent().removeClass('disabled');
						}
					}
				});
				return( $(this) );
			},
			// Disable context menu(s)
			disableContextMenu: function() {
				//$(this).each( function() {
				// $(this).addClass('disabled');
				//});
				//return( $(this) );
				contextMenuEnabled = false;
			},
			// Enable context menu(s)
			enableContextMenu: function() {
				//$(this).each( function() {
				// $(this).removeClass('disabled');
				//});
				//return( $(this) );
				contextMenuEnabled = true;
			},
			// Destroy context menu(s)
			destroyContextMenu: function() {
				// Destroy specified context menus
				$(this).each( function() {
					// Disable action
					$(this).unbind('mousedown').unbind('mouseup');
				});
				return( $(this) );
			}
		});
	})(jQuery);
	/**********************************End：右键菜单插件**********************************************/	
	var MenuItem = SuperMap.Class(
	/**
	 * @lends MenuItem.prototype
	 */
	{
		/**
		 * 标题文字
		 * @type string
		 */
		title: null,
		
		/**
		 * 回调方法
		 * @type function
		 */
		callBack: null,
		
		/**
		 * 图片地址
		 * @type string
		 */
		imageUrl: null,
		
		/**
		 * 上下文
		 */
		context: null,
		
		/**
		 * @param opt {object} 各种属性
		 */
		initialize: function(opt) {
			SuperMap.Util.extend(this,opt);
		}
	});
	
	var MenuStyle = SuperMap.Class(
	/**
	 * @lends MenuStyle.prototype
	 */
	{
		/**
		 * 右键菜单的背景色。
		 * @type string
		 */
		backColor: "#e7e4e2",
		
		/**
		 * 鼠标悬停在某个子项时该子项背景色。
		 * @type function
		 */
		overColor: "#e9f4fc",
		
		/**
		 * 菜单淡入淡出的速度，单位为毫秒。
		 * @type string
		 */
		speed: 500,
				
		/**
		 * @param opt {object} 各种属性
		 */
		initialize: function(opt) {
			SuperMap.Util.extend(this,opt);
		}
	});
	
	var ContextMenu = SuperMap.Class(
	/**
	 * @lends ContextMenu.prototype
	 */
	{
		/**
		 * 地图容器。
		 * @type HTMLDIVElement
		 */
		mapContainer: null,
				
		/**
		 * 右键子项。
		 * @type Array<MenuItem>
		 */
		items: null,
				
		/**
		 * 右键样式。
		 * @type MenuStyle
		 */
		style: null,
		
		/**
		 * 是否有退出按钮
		 * @type boolean
		 */
		quitButton: true,
						
		/**
		 * @param opt {object} 各种属性
		 */
		initialize: function(opt) {
			SuperMap.Util.extend(this,opt);
			this._setCssSheet();
			this._installMenu();
			this._setStyle();
			this._useContextMenu();
		},
		
		dispose:function(){
			///<summary>释放右键菜单资源。</summary>
			//j$(this._container).destroyContextMenu();
		},
		enableMenus:function(enable){
			///<summary>控制右键菜单功能的可用性。</summary>
			///<param name="enable" type="Boolean">true 表示可用，false 表示不可用。</param>
			if(enable == true){
				j$(this.mapContainer).enableContextMenu();
			}
			else if(enable == false){
				j$(this.mapContainer).disableContextMenu();
			}
		},
		
		_setCssSheet:function(){
			var cssString = '.contextMenu{position:absolute;z-index:99999;border:solid 1px #CCC;background:#EEE;padding:0;margin:0;display:none;overflow:hidden;width:150px;}.contextMenu LI{list-style:none;padding:0;margin:0;}.contextMenu A{color:#333;text-decoration:none;display:block;line-height:30px;height:30px;background-position:6px center;background-repeat:no-repeat;outline:none;padding:1px 6px;}.contextMenu LI.hover A{color:black;background-color:' + this.style.overColor + ';}.contextMenu LI.disabled A{color:#AAA;cursor:default;}.contextMenu LI.hover.disabled A{background-color:transparent;}.contextMenu LI.separator{border-top:solid 1px #CCC;}.separatorLine{background:none repeat scroll 0 0 #999999;height:1px;overflow:hidden;margin-top:5px;margin-bottom:5px;}';
			for(var x in this.items){
			if(!!this.items[x] && this.items[x].imageUrl)
			cssString += '.contextMenu LI.' + this.items[x].title + ' A { background-image: url(' + this.items[x].imageUrl + '); }';
			}
			j$("head").append('<style type="text/css">' + cssString + '</style>');
		},
		
		_useContextMenu:function(){
			var main = this;
			var args = null;
			if(this.style.speed){
				args = {
					menu: 'myMenu',
					inSpeed: this.style.speed,
					outSpeed: this.style.speed
				};
			}
			else{
				args = {
					menu: 'myMenu'
				};
			}
			j$(this.mapContainer).contextMenu(args,
				function(action, el, pos) {
					for(var item in main.items){
						if(!!main.items[item] && main.items[item].title == action){
							main.items[item].callBack(main.items[item].context, pos);
							//eval(main.items[item].callBack);
						}
					}
			});
		},
		
		_setStyle:function(){
			var main = this;
			j$("#myMenu").css({"background":"#"+main.style.backColor.slice(1)});
			//j$(".contextMenu li.hover a").css({"background-color":"#"+main.style.overColor.slice(1)});
		},
		
		_installMenu:function(){
			var menuSting = '<ul id="myMenu" class="contextMenu">';
			for(var i=0;i<this.items.length;i++){
				if(this.items[i] === null) {
					menuSting += '<li><div class="separatorLine"></div></li>';
				}
				else{
					menuSting += '<li class="' + this.items[i].title + '"><a id="#' + this.items[i].title + '" href="#' + this.items[i].title + '">' + this.items[i].title + '</a></li>';
				}
			}
			//if(this.quitButton == true)
			// menuSting += '<li class="quit separator"><a href="#quit">退出</a></li>';
			menuSting += '</ul>';
			j$("body").append(menuSting);
		}
	});
	SuperMap.MenuItem = MenuItem;
	SuperMap.MenuStyle = MenuStyle;
	SuperMap.ContextMenu = ContextMenu;
})(window.SuperMap);