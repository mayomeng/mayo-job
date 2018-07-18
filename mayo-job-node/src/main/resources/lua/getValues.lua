local resultList ={}
for i = 1,#(KEYS) do
  local isExists = redis.call('exists', KEYS[i])
  if (isExists == 1)
  then
    resultList[i]=redis.call('get', KEYS[i])
    else
    resultList[i]=false
  end
end 
return resultList