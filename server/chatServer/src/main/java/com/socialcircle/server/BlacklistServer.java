package com.socialcircle.server;

public interface BlacklistServer {
    Boolean ifHeBlack(Integer me, Integer to);

    Boolean ifMeBlack(Integer me, Integer to);
}
