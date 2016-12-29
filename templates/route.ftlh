`${data.busStopEmoji} ${data.stopNumber}`
<#list data.routes as route>
${route.routeName?no_esc} - ${data.busEmoji} ${route.routeCoverage?no_esc}
</#list>