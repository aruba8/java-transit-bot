`Stop ${data.stopNumber}`
Next buses:
<#list data.busses as bus>
${bus.busNumber} (${bus.busName?no_esc}) depart in ${bus.timeBeforeDepart} min. at ${bus.formattedDepartureTime}.
</#list>
