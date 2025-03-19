package com.sea.turtle.soup.turup.service.impl;

import com.sea.turtle.soup.turup.dao.entity.Puzzle;
import com.sea.turtle.soup.turup.dao.mapper.PuzzleMapper;
import com.sea.turtle.soup.turup.service.PuzzleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PuzzleServiceImpl implements PuzzleService {

    @Autowired
    private PuzzleMapper puzzleMapper;
    @Override
    public void insert(Puzzle puzzle) {
        puzzleMapper.insertPuzzle(puzzle);
    }
}
