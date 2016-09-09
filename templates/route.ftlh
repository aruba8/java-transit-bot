`Stop ${data.stopNumber}`
<#list data.routes as route>
${route.routeName?no_esc} - coverage: ${route.routeCoverage?no_esc}
</#list>