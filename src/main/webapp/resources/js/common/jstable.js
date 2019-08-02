var JSContextMenu = function($el,callbackObj){
	this.initialize($el,callbackObj);
};

JSContextMenu.prototype = {
	$el : null,
	callbackObj : null,
	id : "",
	modifyFilename  : null,
	modifyOffice  	: null,
	download  		: null,
	modifyTag 		: null,
	share  			: null,
	sharelink		: null,
	version 		: null,
	del 			: null,
	detail 			: null,
	copyClipboard	: null,
	ContextMenuCallBack : null,

	initialize: function($el,callbackObj) {
		this.$el = $el;
		this.callbackObj = callbackObj;
	},
	init : function(id,row) {
		this.id = id;

		row.trId = id;

		this.setMenuItems(row);

		this.$el.find("#" + id ).contextPopup({
	          items: this.getItems(row)
	    });
	},
	setMenuItems : function(row){
		this.modifyFilename = { $el : this.$el , mode :  "rename"		,label:'이름변경',  	faIcon : "<i class='fa fa-pencil rim-fa' ></i>",            action: this.onClick  , row : row , callbackObj : this.callbackObj};
		this.modifyOffice  	= { $el : this.$el , mode :  "edit"			,label:'문서편집',  	faIcon : "<i class='fa fa-file-text-o rim-fa' ></i>",       action: this.onClick  , row : row , callbackObj : this.callbackObj};
		this.download  		= { $el : this.$el , mode :	 "download"		,label:'다운로드',   	faIcon : "<i class='fa fa-download rim-fa' ></i>", 		action: this.onClick  , row : row , callbackObj : this.callbackObj};
		this.modifyTag 		= { $el : this.$el , mode :  "tag"			,label:'태그수정',  	faIcon : "<i class='fa fa-tag rim-fa' ></i>",   action: this.onClick  , row : row , callbackObj : this.callbackObj};
		this.share  		= { $el : this.$el , mode :  "sharing"		,label:'공유',      	faIcon : "<i class='fa fa-share-alt rim-fa' ></i>",        action: this.onClick  , row : row , callbackObj : this.callbackObj};
		this.sharelink 		= { $el : this.$el , mode :  "sharinglinks"	,label:'링크공유', 	faIcon : "<i class='fa fa-link rim-fa' ></i>",        action: this.onClick  , row : row , callbackObj : this.callbackObj};
		this.version 		= { $el : this.$el , mode :  "version"		,label:'버전',     	faIcon : "<i class='fa fa-refresh rim-fa' ></i>",           action: this.onClick  , row : row , callbackObj : this.callbackObj};
		this.del 			= { $el : this.$el , mode :  "delete"		,label:'삭제',      	faIcon : "<i class='fa fa-trash rim-fa' ></i>",             action: this.onClick  , row : row , callbackObj : this.callbackObj};
		this.detail 		= { $el : this.$el , mode :  "detail"		,label:'속성',     	faIcon : "<i class='fa fa-question-circle rim-fa' ></i>",  action: this.onClick  , row : row , callbackObj : this.callbackObj};
		this.copySharingIn	= { $el : this.$el , mode :  "copySharingIn",label:'복사',  		faIcon : "<i class='fa fa-copy rim-fa' ></i>",        action: this.onClick  , row : row , callbackObj : this.callbackObj};

	},
	onClick : function(event){
		if(typeof onClickContextMenu === "function" ){
			onClickContextMenu(event,$(this).get(0));
		}else{
			alert("JSTable.js onClickContextMenu Error.");
		}
	},
	getItems : function(row){
		if(this.callbackObj.bodyName == "all" ){
			return this.getAllItems(row);
		}
		else if(this.callbackObj.bodyName == "sharingin" ){
			return this.getSharinginItems(row);
		}
		else if(this.callbackObj.bodyName == "sharingout" ){
			return this.getSharingoutItems(row);
		}
		else if(this.callbackObj.bodyName == "bookmark" ){
			return this.getBookMarkItems(row);
		}
		else if(this.callbackObj.bodyName == "search" ){
			return this.getSearchItems(row);
		}
		else if(this.callbackObj.bodyName == "trashbin" ){
			return this.getTrashbinItems(row);
		}
	},
	getAllItems : function(row){
		var items = [] ;

		if(this.isReadOnly()){

			items.push(this.download);
			items.push(null);
			items.push(this.detail);

		}else{

			items.push(this.modifyFilename);
			items.push(this.modifyTag);	

			if( this.isWriteOffice(row.icon) ) {
				
				items.push(null);
				items.push(this.modifyOffice);
			}

			items.push(null);
			items.push(this.download);
			
			if( !isGroupStorage(this.callbackObj.storageId) ){
				items.push(this.share);
			}
			if( this.isFile(row) ){
				items.push(this.sharelink);
			}
			items.push(this.del);

			items.push(null);
			if( this.isFile(row)) {
				items.push(this.version);
			}
			items.push(this.detail);
		}
		return items;
	},
	getSharinginItemsTop : function(row){

		var items = [] ;

		if( row.sharePermissions == "W" ){

			if( this.isWriteOffice(row.icon) ) {
				
				items.push(this.modifyOffice);
			}
		}

		items.push(this.copySharingIn);
		items.push(this.download);
		items.push(null);
		items.push(this.detail);
		return items;
	},
	getSharinginItems : function(row){

		if(this.callbackObj.sharingInTopList == true ){
			return this.getSharinginItemsTop(row);
		}

		var items = [] ;
		
		if(this.isReadOnly()){
			items.push(this.download);

			items.push(null);
			items.push(this.detail);

		}else{

			items.push(this.modifyFilename);
			items.push(this.modifyTag);

			if( this.isWriteOffice(row.icon) ) {
				
				items.push(null);
				items.push(this.modifyOffice);
			}

			items.push(null);
			items.push(this.download);
			if( this.isFile(row) ){
				items.push(this.sharelink);
			}
			items.push(this.del);

			items.push(null);
			items.push(this.detail);
		}

		return items;
	},
	getSharingoutItems : function(row){
		var items = [] ;
		if(this.isReadOnly()){

			items.push(this.download);
			items.push(null);
			items.push(this.detail);

		}else{

			items.push(this.modifyTag);

			if( this.isWriteOffice(row.icon) ) {
				
				items.push(null);
				items.push(this.modifyOffice);
			}

			items.push(null);
			items.push(this.download);
			if( !isGroupStorage(this.callbackObj.storageId) ){
				items.push(this.share);
			}
			items.push(null);
			items.push(this.detail);
		}
		return items;
	},
	getBookMarkItems : function(row){
		var items = [] ;
		if(this.isReadOnly()){

			items.push(this.download);
			items.push(null);
			items.push(this.detail);

		}else{

			items.push(this.modifyTag);

			if( this.isWriteOffice(row.icon) ) {
				
				items.push(null);
				items.push(this.modifyOffice);
			}

			items.push(null);
			items.push(this.download);
			if( !isGroupStorage(this.callbackObj.storageId) ){
				items.push(this.share);
			}
			if( this.isFile(row) ){
				items.push(this.sharelink);
			}
			items.push(null);
			items.push(this.detail);
		}

		return items;
	},
	getSearchItems : function(row){
		
		var items = [] ;
				
		return items;
	},
	getTrashbinItems : function(row){
		var items = [ this.detail ] ;
		return items;
	},
	isWriteOffice : function(ext){

		if (rimCommon.getWebOfficeYn() != "N") {

			ext = ext.toLowerCase();
			var extList = [ "doc","docx","xls","xlsx","ppt","pptx","hwp" ,"hwpx"];
			if( extList.indexOf(ext) != -1 ){
				return true;
			}
		}
		return false;
	},
	isFile : function(row){
		if( row.fileType == "F" )
			return true;
		return false;
	},
	isReadOnly : function(){
		if( getNowPermissions(this.callbackObj) == "R" )
			return true;
		return false;
	}
	
};

var JSTable = function($el,options,callback,callbackObj) {
	this.initialize($el,options,callback,callbackObj);
};

JSTable.prototype = {
	guid : rimCommon.generateUUID(),
	$el : null,
	JSContextMenu : null,
	emptyMessage : "No Data",
	totalNotify : "Total : 0",
	limit : 15,
	nowPageIndex : 1,
	totalPage : 1,
	sortId : '',
	sort : '',
	ContextMenuItems : null,
	enableCheckAll : false,
	enableSearchDate : false,
	enableSearchText : false,
	enablePaging : false,
	enableContextMenu :false,
	enableHeaderSort : false,
	searchCombo : {},
	Headers : {},
	onPaging : null,
	postCallBack : null,
	postCallBackObj : null,

	initialize: function($el,options,callback,callbackObj) {
		var self = this;
		this.$el = $el;
		this.postCallBack = callback;
		this.postCallBackObj = callbackObj;
		if(options){
			if(options.totalNotify)this.totalNotify = options.totalNotify;
			if(options.emptyMessage)this.emptyMessage = options.emptyMessage;
			if(options.enableCheckAll)this.enableCheckAll = options.enableCheckAll;
			if(options.enableSearchDate)this.enableSearchDate = options.enableSearchDate;
			if(options.enableSearchText)this.enableSearchText = options.enableSearchText;
			if(options.enablePaging)this.enablePaging = options.enablePaging;
			if(options.enableContextMenu)this.enableContextMenu = options.enableContextMenu;
			if(options.searchCombo)this.searchCombo = options.searchCombo;
			if(options.Headers)this.Headers = options.Headers;
			if(options.limit)this.limit = options.limit;
			if(options.enableHeaderSort)this.enableHeaderSort = options.enableHeaderSort;
			if(options.onPaging)this.onClickHeaderSort = options.onPaging;
		}
		this.JSContextMenu = new JSContextMenu($el,callbackObj);

		self.setTotalNotify(self.totalNotify);

		self.setCheckAll(self.enableCheckAll);
		self.setSearchDate(self.enableSearchDate);
		self.setSearchText(self.enableSearchText);
		self.setPaging(self.enablePaging);

		$.each( self.searchCombo, function( key, value ) {
			self.addSearchCombo(key,value);
		});
		self.selectSearchCombo(0);

		self.$el.find("#search_text").keydown(function(event){
			if (event.keyCode == 13 && !this.checkKeyEvent) {
				this.checkKeyEvent = true;
				self.search(event);
			}
		}).keyup(function(e){
			this.checkKeyEvent = false;
		});

		self.$el.find("#btnSearch").on("click",function(event){
			self.search(event);
		});

		if(self.enableCheckAll == true){
			self.$el.find("#btnCheckAll").on("click",function(event){
				self.checkAll(event);
			});
		}

		$.each( self.Headers, function( key, value ) {
			self.addHeader(value.id,value.title,value.width,value.sort);
		});

		self.setEmptyRow( self.Headers.length);

	},
	setEmptyRow : function(colspan){
		if(this.enableCheckAll == true){
			colspan = colspan + 1;
		}
		var tr = "<tr class='text-nowrap emptymessage'>";
		tr += "<td class='text-center' colspan='"+ colspan +"' >"+ this.emptyMessage +"</td>";
		tr += "</tr>";
		this.$el.find(".table > tbody").append( tr );
	},
	getTotalPage : function(){
		return  this.totalPage;
	},
	setTotalPage : function(rows){
		 this.$el.find(".pagination").empty();
		 var self = this;
		 this.totalPage = Math.ceil( rows / this.limit );

		 this.$el.find(".pagination").append("<li id='pagination_prev' class='paginate_button'><a href='#'><i class='fa fa-chevron-left' ></i></a></li>");

		 var viewPage = 10;
		 var startPage = (this.nowPageIndex-1);

		 if( (startPage%viewPage) < viewPage  ){
			 startPage = startPage -  (startPage%viewPage);
		 }

		 var endPage = startPage + viewPage;

		 if( endPage >= this.totalPage ){
			 startPage = this.totalPage - viewPage;
			 if(startPage < 0 ){
				 startPage = 0;
			 }
			 endPage = this.totalPage;
		 }
		 for( var i = startPage ; i < endPage  ; i++ ){
			 var idx = i + 1;
			 var li = "<li id='pagination_"+ idx +"' class='paginate_button";
			 if( idx ==  this.nowPageIndex ){
				 li += " active";
			 }
			 li += "'>";
			 li += 	"<a href='#' >";
			 li += 	idx;
			 li += "</a>";
			 li += "</li>";

			 this.$el.find(".pagination").append(li);

			 this.$el.find("#pagination_" + idx ).on("click",function(event){
				var id = $(this).attr("id").replace("pagination_","");
				self.onClickPaging(id);
			});
		 }

		 this.$el.find(".pagination").append("<li id='pagination_next' class='paginate_button'><a href='#'><i class='fa fa-chevron-right' ></i>  </a></li>");

		 this.$el.find("#pagination_prev").on("click",function(event){
			var idx = parseInt(parseInt(self.nowPageIndex) - 1);
			if( idx <= 0 ){
				idx = 1;
			}
			if( self.nowPageIndex != idx )
				self.onClickPaging(idx);
		});

		this.$el.find("#pagination_next").on("click",function(event){
			var idx = parseInt(parseInt(self.nowPageIndex) + 1);
			if( idx >=  self.totalPage ){
				idx = self.totalPage;
			}
			if( self.nowPageIndex != idx )
				self.onClickPaging(idx);
		});

		this.setTotalNotify("Total : " +  rows );
	},
	setPagingMap : function(pagingMap){
		this.nowPageIndex = pagingMap.nowPageIndex;
		this.$el.find("#search_text").val(pagingMap.searchText);
		this.$el.find("#search_column").val(pagingMap.searchId);
		if(pagingMap.fromDate != null  && pagingMap.toDate != null ){
			this.$el.find('#search_date').val(pagingMap.fromDate + " ~ " + pagingMap.toDate);
		}
	},
	onClickPaging : function(idx){
		this.nowPageIndex = parseInt(idx);
		this.$el.find(".pagination .active").removeClass("active");
		this.$el.find("#pagination_" + idx).addClass("active");
		if(typeof this.postCallBack === "function" ){
			this.postCallBack.apply(this.postCallBackObj);
		}else{
			alert("JSTable.js Error.");
		}

	},
	getSearchObject : function(){
		var postData = {};
		if(this.enableSearchDate && this.$el.find('#search_date').val() != null ){
			if(this.$el.find('#search_date').val().indexOf("~") != -1 ){
				var searchDate = this.$el.find('#search_date').val().split('~');
				if(searchDate.length == 2 ){
					postData.rim_from_date = searchDate[0].trim();
					postData.rim_to_date =  searchDate[1].trim();
				}
			}
		}
		if(this.enableSearchText){
			postData.rim_search_id = this.$el.find("#search_column option:selected").val();
			postData.rim_search_text = this.$el.find("#search_text").val();
		}
		if(this.enablePaging){
			postData.rim_limit  = this.limit;
			postData.rim_offset = this.limit * (this.nowPageIndex - 1);
			postData.rim_now_page_index = this.nowPageIndex;
		}
		if( this.enableHeaderSort ){
			postData.rim_sort_id = this.sortId;
			postData.rim_sort = this.sort;
		}
		return postData;
	},
	setCheckAll : function(flag){
		var tr = "<tr class='text-nowrap'>";
		if(flag == true ){
			tr += "<th style='width:5px;' class='text-center'>";
			tr += "<input type='checkbox' id='btnCheckAll' name='btnCheckAll'>";
			tr += "</th>";
		}
		tr += "</tr>";
		this.$el.find(".table > thead").append(tr);
	},
	setSearchDate : function(flag){
		if(!flag) return;
		if( flag != true )
			return;

		this.$el.find(".search-date").removeClass("hidden");
		this.$el.find("#btnSearch").removeClass("hidden");

	},
	setSearchText : function(flag){
		if(!flag) return;
		if( flag != true )
			return;

		this.$el.find(".search-text").removeClass("hidden");
		this.$el.find("#btnSearch").removeClass("hidden");

	},
	setPaging : function(flag){
		if(!flag) return;
		if( flag != true )
			return;

		this.$el.find(".JSTablePaging").removeClass("hidden");
	},
	setTotalNotify : function(msg){
		this.$el.find("#totalNotify").text(msg);
	},
	search : function(event){
		this.nowPageIndex = 1;
		if(typeof this.postCallBack === "function" ){
			this.postCallBack.apply(this.postCallBackObj);
		}else{
			alert("JSTable.js ERROR.");
		}
	},
	checkAll : function(event){
		var checked = this.$el.find("#btnCheckAll").is(":checked");
		var checkbox = this.$el.find("table > tbody input[type=checkbox]");
		if(checked){
			checkbox.prop("checked",true).trigger("change");

		}else{
			checkbox.prop("checked",false).trigger("change");
		}
	},
	getCheckedRowCount : function(){
		var checkbox = this.$el.find("table > tbody input[type=checkbox]:checked");
		var listCheckBoxCount =  this.$el.find("table > tbody input[type=checkbox]").length;
		if(checkbox){
			if(checkbox.length == 0 || listCheckBoxCount > checkbox.length ){
				this.$el.find("#btnCheckAll").prop("checked",false);
			}
			else if(listCheckBoxCount == checkbox.length ){
				this.$el.find("#btnCheckAll").prop("checked",true);
			}
			return checkbox.length;
		}
		this.$el.find("#btnCheckAll").prop("checked",false);
		return 0;
	},
	getCheckedRowValue : function(){
		var i = 0;
		var fileIds = "";
		this.$el.find("table > tbody input[type=checkbox]:checked").each(function() {
			if(i == 0 ){
				fileIds = $(this).val();
			}else{
				fileIds += "," + $(this).val();
			}
			i++;
		});
		return fileIds;
	},
	getCheckedRowData : function(){
		var self = this;
		var i = 0;
		var rowData = [];
		this.$el.find("table > tbody input[type=checkbox]:checked").each(function() {
			var trId = $(this).attr("data-trId");

			var tr = self.$el.find("#" + trId );
			var row = self.getJSDataStringToJson(tr.attr("JSData"));
			rowData.push(row);
			i++;
		});
		return rowData;
	},
	addSearchCombo : function (value,text){
		this.$el.find("#search_column").append("<option value='"+value+"'>"+text+"</option>");
	},
	selectSearchCombo : function(index){
		this.$el.find("#search_column").find("option:eq("+ index +")").prop("selected", true);
	},
	addHeader : function(id,title,width,sort){
		var th = "<th id='header_sort_"+id+"' style='width:";
		th += width + ";";

		if(  this.enableHeaderSort == true && sort == true ) {
			th += "cursor: pointer;";
		}

		th += "' class='text-center'>";
		th += title;
		if( this.enableHeaderSort == true && sort == true){
			th += "<i class='fa fa-unsorted' ></i>";
		}
		th += "</th>";
		this.$el.find(".table > thead > tr").append( th );

		var self = this;
		if ( this.enableHeaderSort == true && sort == true  ) {
			self.$el.find("#header_sort_" + id).on("click",function(event){
				self.onClickHeaderSort(id);
			});
    	}
	},
	onClickHeaderSort : function(id){
		this.sortId = id;
		if(this.$el.find("#header_sort_" + id).find(".fa").hasClass("fa-sort-asc") ){
			this.$el.find(".table > thead > tr .fa").removeClass("fa-sort-desc").removeClass("fa-sort-asc");
			this.$el.find("#header_sort_" + id).find(".fa").addClass("fa-sort-desc");
			this.sort = "DESC";
		}else{
			this.$el.find(".table > thead > tr .fa").removeClass("fa-sort-desc").removeClass("fa-sort-asc");
			this.$el.find("#header_sort_" + id).find(".fa").addClass("fa-sort-asc");
			this.sort = "ASC";
		}
		if(typeof this.postCallBack === "function" ){
			this.postCallBack.apply(this.postCallBackObj);
		}else{
			alert("JSTable.js ERROR.");
		}
	},
	addRow : function(row){

		this.$el.find(".table > tbody").find(".emptymessage").remove();

		var self = this;
		var oldTr = this.$el.find("#" + $(row).attr('id') );

		if( oldTr.attr("JSData") === undefined ){
			self.$el.find(".table > tbody").append( row );
		}else{
			oldTr.replaceWith(row);
			self.$el.find("#" + $(row).attr('id') ).children().hide().fadeIn(1000);
		}

		if(self.enableContextMenu == true ){
			self.JSContextMenu.init($(row).attr('id'),self.getJSDataStringToJson($(row).attr("JSData")));
		}

		self.$el.find("#" + $(row).attr('id') ).on("click",function(event){
			self.onClick(self.getJSDataStringToJson($(this).attr("JSData")));
		});

		this.getCheckedRowCount();
	},
	removeRow : function(row){
		var self = this;
		var oldTr = this.$el.find("#" + row.trId );
		if( oldTr.attr("JSData") === undefined ){

		}else{
			oldTr.fadeOut('slow',function(){
			    $(this).remove();
			    self.getCheckedRowCount();
			});
		}
	},
	isRowByPath : function(path){
		var self = this;
		var result = false;
		var rows = this.$el.find(".table > tbody > tr");
		$.map(rows, function (row) {

			var rowJSdata = $(row).attr("JSData");
			if( rowJSdata ==undefined ) {
				result = false;
			} else {

				var data = self.getJSDataStringToJson($(row).attr("JSData"));
				if( data.path == path ){
					result = true;
					return;
				}
			}
		});
		return result;
	},
	getJSDataStringToJson : function(data){
		var dec = JSCrypto.decryptByAES(data,this.guid);
		return JSON.parse( dec );
	},

	getJsonToJSDataString : function(data){
		var s = JSON.stringify(data);
		return JSCrypto.encryptByAES(s,this.guid);
	},

	getStringToJson : function(data){
		return JSON.parse(data);
	},
	getJsonToString : function(data){
		return JSON.stringify(data);
	},
	empty : function(flag){
		this.$el.find(".table > tbody").empty();
		this.setEmptyRow(this.Headers.length);
		if(flag == true ){
			this.clearValue();
		}
	},
	onClick : function(row){
	},
	clearValue : function(){
		this.$el.find("input[type=text]").val('');
		this.$el.find("input[type=number]").val('');
		this.$el.find("input[type=checkbox]").attr('checked', false);
		this.$el.find(".table > thead > tr .fa").removeClass("fa-sort-desc").removeClass("fa-sort-asc").addClass("fa-unsorted");
		this.$el.find(".pagination").empty();
		this.setTotalNotify("Total : 0");
		this.selectSearchCombo(0);
		this.totalPage = 1,
		this.nowPageIndex = 1;
		this.sortId = '';
		this.sort = '';

	},
	reInit : function(){
		this.$el.find(".table > thead").empty();
		this.$el.find(".table > tbody").empty();
		this.clearValue();
	}

};

