package com.socialCircle.vm;

import com.socialCircle.entity.Dynamic;
import com.socialCircle.entity.Image;
import com.socialCircle.entity.Topic;
import lombok.Data;

import java.util.List;

@Data
public class DynamicVM {
    private Dynamic dynamic;
    private List<Image> images;
    private Topic topic;
}
