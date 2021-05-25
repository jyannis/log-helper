package top.jyannis.loghelper.holder;

import java.util.Map;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/23
 */
public class LogFilterChainHolder {

    private Map<String, String> filterChainDefinitionMap;

    public LogFilterChainHolder() {
    }

    public LogFilterChainHolder(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

}
