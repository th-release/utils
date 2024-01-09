package utils

type BasicResponse struct {
	Success bool        `json:"success"`
	Message *string     `json:"message"`
	Data    interface{} `json:"data"`
}

func GetBasicResponse(response BasicResponse) BasicResponse {
	return BasicResponse{
		Success: response.Success,
		Message: response.Message,
		Data:    response.Data,
	}
}
