package mayfly.sys.module.machine.mapper;

import mayfly.core.base.mapper.BaseMapper;
import mayfly.sys.module.machine.entity.MachineMonitorDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:03 下午
 */
public interface MachineMonitorMapper extends BaseMapper<MachineMonitorDO> {

    @Select("<script>" +
            "SELECT " +
            "AVG( cpu_rate ) AS cpuRateAvg," +
            "AVG( mem_rate ) AS memRateAvg," +
            "AVG( one_min_loadavg ) AS oneMinLoadavg," +
            "AVG( five_min_loadavg ) AS fiveMinLoadavg," +
            "AVG( fif_min_loadavg ) AS fifMinLoadavg," +
            "DATE_FORMAT( create_time, '%Y-%m-%d/%H:00' ) AS createTime " +
            "FROM " +
            "tb_machine_monitor " +
            "WHERE " +
            "machine_id = #{machineId} " +
            "AND DATE_FORMAT( create_time, '%Y-%m-%d' ) >= #{beginTime} " +
            "AND DATE_FORMAT( create_time, '%Y-%m-%d' ) &lt;= #{endTime} " +
            "GROUP BY createTime" +
            "</script>")
    List<Map<String, Object>> selectByDate(@Param("machineId") Long machineId, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
}
