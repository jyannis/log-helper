package top.jyannis.loghelper.holder;

import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import top.jyannis.loghelper.domain.LogMode;

import java.util.*;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/26
 */
public class LogFilterChainHolder {

    private LinkedHashMap<String, String> filterChainDefinitionMap;

    private PathMatcher pathMatcher = new AntPathMatcher();

    public LogFilterChainHolder() {
    }

    public LogFilterChainHolder(LinkedHashMap<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public LinkedHashMap<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public void setFilterChainDefinitionMap(LinkedHashMap<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public String matches(String path){
        //从后往前找到最匹配
        ListIterator<Map.Entry<String, String>> li = new ArrayList<>(filterChainDefinitionMap.entrySet()).listIterator(filterChainDefinitionMap.size());
        while(li.hasPrevious()) {
            Map.Entry<String, String> entry = li.previous();
            if(pathMatcher.match(entry.getKey(),path))return entry.getValue();
        }
        return LogMode.NONE;
    }

}
