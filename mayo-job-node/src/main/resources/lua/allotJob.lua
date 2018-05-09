for var=1, ARGV[1], 1 do
	redis.call("RPOPLPUSH", KEYS[1], KEYS[2])
end