package main

// go get golang.org/x/crypto/sha3

import (
	"encoding/hex"

	"golang.org/x/crypto/sha3"
)

func hash(input string) (string, error) {
	hasher := sha3.New256()
	hasher.Write([]byte(input))
	hashBytes := hasher.Sum(nil)

	// 16진수 문자열로 변환
	hashString := hex.EncodeToString(hashBytes)

	return hashString, nil
}
