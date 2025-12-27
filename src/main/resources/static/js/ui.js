export function $(id){
    return document.getElementById(id);
}

export function showJSON(id,data){
    $(id).innerHTML = `<pre>${JSON.stringify(data,null,2)}</pre>`;
}

export function toast(msg){
    alert(msg);
}
