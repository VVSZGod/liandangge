package com.jiamian.translation.entity.response;

import com.jiamian.translation.entity.dto.MetaDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: ModelResponse
 * @Auther: z1115
 * @Date: 2023/3/26 11:12
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class ModelDetailResponse {

    private Long id;

    private Long modelId;

    private String name;

    private String type;

    //@JsonFormat(timezone = "yyyy-MM-dd HH:mi:ss")
    private LocalDateTime createDate;

    private String modelUrl;

    private String description;

    private List<MetaDTO> metaDTOList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }


    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    public List<MetaDTO> getMetaDTOList() {
        return metaDTOList;
    }

    public void setMetaDTOList(List<MetaDTO> metaDTOList) {
        this.metaDTOList = metaDTOList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }
}
