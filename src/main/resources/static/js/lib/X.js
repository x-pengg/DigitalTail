;(function(W){

    var prefix = '!',index;
    W.onhashchange = function () {
        var view = W.location.hash.substring(prefix.length+1);
        if(!view){
            return X.redirect(index);
        }

        var views = X.views,
            arg = view.split('/');
        ctrl = arg[0];
        arg.shift();
        for(var i=0;i<views.length;i++){
            if (ctrl === views[i].view){
                views[i].callback.apply(W, arg);
            }
        }

        if(W.pop)
            W.pop.call(W, ctrl);
    }

    W.X = {
        views:[],
        get:function(view,callback){

            if (!view)
                return;

            if (callback == undefined)
                callback = function () {}

            if (view instanceof Array) {

                for(x in view){
                    this.get(view[x],callback);
                }

            }else if(typeof view == 'string'){

                // update
                for(var i=0;i<this.views.length;i++){
                    if (view === this.views[i].view){
                        this.views[i] = callback;
                        break;
                    }
                }

                this.views.push({view:view, callback:callback });
            }

            return this;
        },
        redirect:function (view) {
            console.log('redirect to ' + view); 
            location.hash='#'+prefix+view;
            return this;
        },
        init:function(arg){
            arg = arg || {};
            prefix = arg.prefix !== undefined ? arg.prefix : prefix;
            index = arg.index||'home';
            pop = arg.pop;
            W.onhashchange();
            return this;
        },
        v:function () {
            console.log('X.js version "1.0_beta"  | i@ridog.me');
        }
    }

})(this);
