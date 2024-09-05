package net.sunday.cloud.base.common.entity.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Schema(description = "分页结果")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class PageResult<T> implements Serializable {

    @Schema(description = "数据")
    private List<T> records;

    @Schema(description = "总量")
    private Long total;

    public PageResult(Long total) {
        this.records = Collections.emptyList();
        this.total = total;
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(0L);
    }

    public static <T> PageResult<T> empty(Long total) {
        return new PageResult<>(total);
    }

}
