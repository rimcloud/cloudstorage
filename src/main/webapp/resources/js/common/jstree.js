var JSTree = function(id,pageClass,clickCallBack,dblclickCallBack){
	this.initialize(id,pageClass,clickCallBack,dblclickCallBack);
};

JSTree.prototype = {
	id : null,
	treeview : null,
	clickedNodeId : null,
	coreNodes : [],

	initialize: function(id,pageClass,clickCallBack,dblclickCallBack) {

		this.id  = id;
		this.treeview = $('#'+id);
		this.treeview.jstree({ 'core' : {
			check_callback : true,
			multiple : false

 		} });

		this.treeview.on('changed.jstree',{ self : this }, function (e, data) {
			for(var i = 0 ; i < data.selected.length ; i++ ){
 				var nodeData = data.instance.get_node(data.selected[i]);
 				if( e.data.self.clickedNodeId != nodeData.id ){
 					e.data.self.clickedNodeId = nodeData.id;
 	 				clickCallBack.apply(pageClass,[nodeData.data]);
 				}
 			}
 		  });

		this.treeview.on('select_node.jstree', function (e, data) {
 		});

		this.treeview.on("dblclick.jstree", function (e) {
			var nodeData =  $(this).jstree("get_node", $("#" + $(this).jstree('get_selected')));
	        if(typeof dblclickCallBack === "function"){
	            	dblclickCallBack.apply(pageClass,[nodeData.data]);
	        }

	    });

	},
	setCoreData : function(callbackRefresh){

		this.treeview.jstree(true).settings.core.data = this.coreNodes;
		this.treeview.jstree(true).refresh();

		this.treeview.on('refresh.jstree', { self : this } , function (e, data) {
			if(typeof callbackRefresh === "function" ){
				callbackRefresh();
			}
			e.data.self.coreNodes.length = 0;
		});
	},
	setCoreNode : function(parentId,id,text,path,opened,selected,icon,data){

		if(rimCommon.isEmpty(parentId) ){
			parentId = "#";
		}
		else{
			var result = $.grep(this.coreNodes, function(e){ return e.id == parentId; });
			if (result.length == 0) {
			  parentId = "#";
			}
		}

		if(rimCommon.isEmpty(opened) ){
			opened = false;
		}

		if(rimCommon.isEmpty(selected) ){
			selected = false;
		}
		if(rimCommon.isEmpty(icon) ){
			icon = "fa fa-rim-tree fa-folder";
		}
		var node =  { "parent" : parentId, "id" : id, "text" : text, "state" : { "opened" : opened, "selected" : selected } , "icon" : icon ,  "data" : { parentId : parentId, id : id, "path" : path , "name" : text }};

		if( !rimCommon.isEmpty(data) ){
			$.extend( node.data , data );
		}

		this.coreNodes.push(node);
	},
	addNode : function(parentId,id,text,path,opened,selected,icon,data){

		if(rimCommon.isEmpty(opened) ){
			opened = false;
		}

		if(rimCommon.isEmpty(selected) ){
			selected = false;
		}
		if(rimCommon.isEmpty(icon) ){
			icon = "fa fa-rim-tree fa-folder";
		}

		var oldNode = this.getNode(id);
		if( !rimCommon.isEmpty(oldNode) ){
			if(oldNode.text != text ) {
				this.updateNode(oldNode.id,text);
			}
			return;
		}

		if( rimCommon.isEmpty(this.getNode(parentId)) ){
			var treeData = { parentId : null, id : id, "path" : path , "name" : text };
			if( !rimCommon.isEmpty(data) ){
				$.extend(treeData , data );
			}
			this.treeview.jstree("create_node", null , { "id" : id, "text" : text, "state" : { "opened" : opened, "selected" : selected } , "icon" : icon ,  "data" : treeData },"last", false, false);
		}else{
			var treeData =  { parentId : parentId , id : id,  "path" : path , "name" : text };
			if( !rimCommon.isEmpty(data) ){
				$.extend(treeData , data );
			}
			this.treeview.jstree("create_node", $("#"+ parentId )  , { "id" : id, "text" : text, "state" : { "opened" : opened, "selected" : selected } , "icon" : icon ,  "data" : treeData },"last", false, false);
			if(opened){
				this.openNode(parentId);
			}
		}
	},
	addNodeData : function(parentId,id,text,data,opened,selected,icon){

		if(rimCommon.isEmpty(opened) ){
			opened = false;
		}

		if(rimCommon.isEmpty(selected) ){
			selected = false;
		}
		if(rimCommon.isEmpty(icon) ){
			icon = "fa fa-rim-tree fa-folder";
		}

		var oldNode = this.getNode(id);
		if( !rimCommon.isEmpty(oldNode) ){
			if(oldNode.text != text ) {
				this.updateNode(oldNode.id,text);
			}
			return;
		}
		data.id = id;
		data.name = text;
		if( rimCommon.isEmpty(this.getNode(parentId)) ){
			data.parentId = null;
			this.treeview.jstree("create_node", null , { "id" : id, "text" : text, "state" : { "opened" : opened, "selected" : selected } , "icon" : icon ,  "data" : data },"last", false, false);
		}else{
			data.parentId = parentId;
			this.treeview.jstree("create_node", $("#"+ parentId )  , { "id" : id, "text" : text, "state" : { "opened" : opened, "selected" : selected } , "icon" : icon ,  "data" : data },"last", false, false);
			if(opened){
				this.openNode(parentId);
			}
		}
	},
	deleteNode : function(id){
		this.treeview.jstree("delete_node", $("#" + id));
	},
	getNode : function(id){
		var node = this.treeview.jstree("get_node", $("#" + id));
		return node;
	},
	selectNode : function(id){
		this.deSelectAll();
		this.openNode(id);
		return this.treeview.jstree("select_node", $("#" + id));
	},
	selectedNode : function(){
		var node = this.treeview.jstree("get_node", $("#" + this.treeview.jstree('get_selected') ));
		return node.data;
	},
	openNode : function(id){
		this.treeview.jstree("open_node", $("#" + id),false,true);
	},
	openNodes : function( arrayId ){
		for ( var idx in arrayId) {
			var id = arrayId[idx];
			this.openNode(id);
		}
	},
	deSelectAll : function(){
		this.treeview.jstree('deselect_all');
	},

	updateNode : function(id,text){
		this.treeview.jstree('set_text',$("#" + id),text);
		var data = this.selectedNode();
		if( !rimCommon.isEmpty(data.name) ) {
			data.name = text;
		}
	},
	destory : function(){
		this.treeview.jstree("destroy");
	}
};