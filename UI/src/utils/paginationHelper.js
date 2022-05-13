import {CURRENT, NEXT, PREV} from "../Components/Pages/Constants";
import ApiCalls from "../api/apiCalls";

export const getPaths = (linkHeader) => {
    if (linkHeader === "") return {}
    const links = linkHeader.split(',')
    let paths = {}
    for (let i = 0; i < links.length; i+=1) {
        let link = links[i];
        let a = link.split(';');
        let path = a[0].slice(1, -1);
        let rel = a[1].slice(4);
        paths[rel] = path;
    }
    return paths;
}
