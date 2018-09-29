package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentoringModel {
    private String mento;
    private List<String> mentis;
}
