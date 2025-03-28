package ru.noir74.shop.misc.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PageSizing {
    PAGE10(10),
    PAGE20(20),
    PAGE50(50),
    PAGE100(100);

    private final int value;
}
