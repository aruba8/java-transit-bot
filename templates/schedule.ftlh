`${data.busStopEmoji} ${data.stopNumber}`
Next buses:
<#list data.busses as bus>
${bus.busNumber} ${data.busEmoji} depart in ${bus.timeBeforeDepart} min. at ${bus.formattedDepartureTime}.
</#list>
