function com_google_code_apis_rest_RestDescribe(){var k=window,j=document,s=k.external,t,p,o='',w={},E=[],B=[],n=[],y,A;if(!k.__gwt_stylesLoaded){k.__gwt_stylesLoaded={};}if(!k.__gwt_scriptsLoaded){k.__gwt_scriptsLoaded={};}var x=k.onload;k.onload=function(a){if(x){k.onload=x;k.onload(a);}p=true;v();};function u(){try{return s&&(s.gwtOnLoad&&k.location.search.indexOf('gwt.hybrid')== -1);}catch(a){return false;}}
function v(){if(t&&p){t(y,'com.google.code.apis.rest.RestDescribe',o);}}
function r(){var f,e;j.write('<script id="__gwt_marker_com.google.code.apis.rest.RestDescribe"><\/script>');e=j.getElementById('__gwt_marker_com.google.code.apis.rest.RestDescribe');if(e){f=e.previousSibling;}function c(b){var a=b.lastIndexOf('/');return a>=0?b.substring(0,a+1):'';}
;if(f&&f.src){o=c(f.src);}if(o==''){o=c(j.location.href);}else if(o.match(/^\w+:\/\//)){}else{var d=j.createElement('img');d.src=o+'clear.cache.gif';o=c(d.src);}if(e){e.parentNode.removeChild(e);}}
function z(){var f=document.getElementsByTagName('meta');for(var d=0,g=f.length;d<g;++d){var e=f[d],h=e.getAttribute('name'),b;if(h){if(h=='gwt:property'){b=e.getAttribute('content');if(b){var i,c=b.indexOf('=');if(c>=0){h=b.substring(0,c);i=b.substring(c+1);}else{h=b;i='';}w[h]=i;}}else if(h=='gwt:onPropertyErrorFn'){b=e.getAttribute('content');if(b){try{A=eval(b);}catch(a){alert('Bad handler "'+b+'" for "gwt:onPropertyErrorFn"');}}}else if(h=='gwt:onLoadErrorFn'){b=e.getAttribute('content');if(b){try{y=eval(b);}catch(a){alert('Bad handler "'+b+'" for "gwt:onLoadErrorFn"');}}}}}}
function m(a,b){return b in E[a];}
function l(a){var b=w[a];return b==null?null:b;}
function D(d,e){var a=n;for(var b=0,c=d.length-1;b<c;++b){a=a[d[b]]||(a[d[b]]=[]);}a[d[c]]=e;}
function q(d){var e=B[d](),b=E[d];if(e in b){return e;}var a=[];for(var c in b){a[b[c]]=c;}if(A){A(d,a,e);}throw null;}
B['locale']=function(){try{var g;if(g==null){var b=location.search;var h=b.indexOf('locale');if(h>=0){var e=b.substring(h);var c=e.indexOf('=')+1;var d=e.indexOf('&');if(d== -1){d=e.length;}g=e.substring(c,d);}}if(g==null){g=l('locale');}if(g==null){return 'default';}while(!m('locale',g)){var f=g.lastIndexOf('_');if(f== -1){g='default';break;}else{g=g.substring(0,f);}}return g;}catch(a){alert('Unexpected exception in locale detection, using default: '+a);return 'default';}};E['locale']={'ca':0,'de':1,'default':2,'en':3};B['user.agent']=function(){var d=navigator.userAgent.toLowerCase();var b=function(a){return parseInt(a[1])*1000+parseInt(a[2]);};if(d.indexOf('opera')!= -1){return 'opera';}else if(d.indexOf('webkit')!= -1){return 'safari';}else if(d.indexOf('msie')!= -1){var c=/msie ([0-9]+)\.([0-9]+)/.exec(d);if(c&&c.length==3){if(b(c)>=6000){return 'ie6';}}}else if(d.indexOf('gecko')!= -1){var c=/rv:([0-9]+)\.([0-9]+)/.exec(d);if(c&&c.length==3){if(b(c)>=1008)return 'gecko1_8';}return 'gecko';}return 'unknown';};E['user.agent']={'gecko':0,'gecko1_8':1,'ie6':2,'opera':3,'safari':4};com_google_code_apis_rest_RestDescribe.onScriptLoad=function(a){com_google_code_apis_rest_RestDescribe=null;t=a;v();};r();z();var C;try{D(['de','safari'],'0BE0C08876C429CB75BDE2729C038D6D');D(['de','ie6'],'52486A260933C2804442F4E463EFD893');D(['ca','ie6'],'5682923731B5EC4A6C3E7DDBDD91E199');D(['default','opera'],'7E861CD44F648D566DD0F444F3898316');D(['en','opera'],'7E861CD44F648D566DD0F444F3898316');D(['default','gecko'],'8A4B86ABAE6CDDFF1994A1C54A49D993');D(['en','gecko'],'8A4B86ABAE6CDDFF1994A1C54A49D993');D(['de','opera'],'8EC99D1B54C43F47A5D9CA827077E500');D(['de','gecko1_8'],'9A65768D195A1E1E9C62554148500600');D(['default','gecko1_8'],'B98BB7D3C80710B2A4AC446D1EFAA694');D(['en','gecko1_8'],'B98BB7D3C80710B2A4AC446D1EFAA694');D(['en','ie6'],'BD491FF0B9DD162CC4F43C5DF8D7FD1F');D(['default','ie6'],'BD491FF0B9DD162CC4F43C5DF8D7FD1F');D(['ca','safari'],'C6DC92512F7EB6364F1951F4E633A861');D(['ca','gecko'],'DBB333566831A1BE2DD36DD9954F2D6F');D(['ca','gecko1_8'],'DF313BD48A9F95F7F8281C6BAD015501');D(['en','safari'],'E0DEC1EDA5B2E0ABDB29C4E9D4591D9D');D(['default','safari'],'E0DEC1EDA5B2E0ABDB29C4E9D4591D9D');D(['de','gecko'],'E57CDF91DB36D4303FDAD2997DEA46C5');D(['ca','opera'],'E5F3E97ED773EC01CE9E9EF276EC9814');C=n[q('locale')][q('user.agent')];}catch(a){return;}C+='.cache.js';if(!__gwt_stylesLoaded['RestDescribe.css']){__gwt_stylesLoaded['RestDescribe.css']=true;document.write('<link rel="stylesheet" href="'+o+'RestDescribe.css">');}j.write('<script src="'+o+C+'"><\/script>');}
com_google_code_apis_rest_RestDescribe.__gwt_initHandlers=function(i,e,j){var d=window,g=d.onresize,f=d.onbeforeunload,h=d.onunload;d.onresize=function(a){i();if(g)g(a);};d.onbeforeunload=function(a){var c=e();var b;if(f)b=f(a);if(c!==null)return c;return b;};d.onunload=function(a){j();if(h)h(a);};};com_google_code_apis_rest_RestDescribe();