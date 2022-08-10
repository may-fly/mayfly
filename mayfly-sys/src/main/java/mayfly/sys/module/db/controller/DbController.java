package mayfly.sys.module.db.controller;

import mayfly.core.model.result.Response2Result;
import mayfly.core.permission.LoginAccount;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.module.db.controller.form.DbSqlSaveForm;
import mayfly.sys.module.db.controller.vo.ColumnVO;
import mayfly.sys.module.db.controller.vo.DbVO;
import mayfly.sys.module.db.controller.vo.TableVO;
import mayfly.sys.module.db.entity.DbSqlDO;
import mayfly.sys.module.db.service.DbService;
import mayfly.sys.module.db.service.DbSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-02 5:41 下午
 */
@Response2Result
@RestController
@RequestMapping("/devops/dbs")
public class DbController {
    @Autowired
    private DbService dbService;
    @Autowired
    private DbSqlService dbSqlService;

    @GetMapping()
    public List<DbVO> getDbs() {
        return BeanUtils.copy(dbService.listAll(), DbVO.class);
    }

    @GetMapping("/{dbId}/t-metadata")
    public List<TableVO> getTables(@PathVariable Long dbId) {
        return dbService.listTable(dbId);
    }

    @GetMapping("/{dbId}/c-metadata")
    public List<ColumnVO> getColumns(@PathVariable Long dbId, @RequestParam("table") String table) {
        return dbService.listColumn(dbId, table);
    }

    @GetMapping("/{dbId}/select-data")
    public List<Map<String, Object>> selectData(@PathVariable Long dbId, @RequestParam("sql") String sql) {
        return dbService.selectData(dbId, sql);
    }

    @GetMapping("/{dbId}/hint-tables")
    public Map<String, List<String>> hintTables(@PathVariable Long dbId) {
        return dbService.hintTables(dbId);
    }

    @PostMapping("/{dbId}/sql")
    public void saveSql(@PathVariable Long dbId, @RequestBody @Valid DbSqlSaveForm sql) {
        Long uid = LoginAccount.getLoginAccountId();
        DbSqlDO cond = new DbSqlDO();
        cond.setCreatorId(uid);
        cond.setDbId(dbId);
        DbSqlDO ds = dbSqlService.getByCondition(cond);
        if (ds != null) {
            DbSqlDO update = new DbSqlDO();
            update.setId(ds.getId());
            update.setSql(sql.getSql());
            dbSqlService.updateByIdSelective(update);
            return;
        }

        ds = new DbSqlDO();
        ds.setDbId(dbId);
        ds.setSql(sql.getSql());
        dbSqlService.insertSelective(ds);
    }

    @GetMapping("/{dbId}/sql")
    public String getDbSql(@PathVariable Long dbId) {
        DbSqlDO cond = new DbSqlDO();
        cond.setCreatorId(LoginAccount.getLoginAccountId());
        cond.setDbId(dbId);
        return Optional.ofNullable(dbSqlService.getByCondition(cond)).map(DbSqlDO::getSql).orElse("");
    }
}
