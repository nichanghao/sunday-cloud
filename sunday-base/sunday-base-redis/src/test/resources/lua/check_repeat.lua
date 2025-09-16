-- KEYS[1] = zset key
-- ARGV[1] = member (string)
-- ARGV[2] = score (number, timestamp; same unit as window)
-- ARGV[3] = window (number, e.g. 60) -- time window in same unit as score
-- ARGV[4] = ttl (seconds) -- key TTL in seconds

local key = KEYS[1]
local member = ARGV[1]
local score = tonumber(ARGV[2])
local window = tonumber(ARGV[3]) or 60
local ttl = tonumber(ARGV[4]) or 30

-- get existing score for this member (if any)
local existing = redis.call('ZSCORE', key, member)
if existing then
  local existing_num = tonumber(existing)
  if existing_num >= (score - window) then
    -- member exists within the time window:
    -- update the zset score (to keep it fresh) and refresh TTL, then return member
    redis.call('ZADD', key, score, member)
    redis.call('EXPIRE', key, ttl)
    return member
  end
end

-- not found within window: add member and refresh TTL, return nil
redis.call('ZADD', key, score, member)
redis.call('EXPIRE', key, ttl)
return nil