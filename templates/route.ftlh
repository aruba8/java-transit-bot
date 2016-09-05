Routes available on this stop:
<#list routes as route>
${route.routeName?no_esc} - coverage: ${route.routeCoverage?no_esc}
</#list>