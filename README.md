# CraftingStoreTekkitPlugin

This plugin allows you to connect your Tekkit Classic server to your CraftingStore store, since the official plugin only supports as 1.7+!

Tested and working with Tekkit Classic (Minecraft 1.2.5).

The code is janky, but it works. I wouldn't recommend doing this yourself unless you happen to be a masochist, in which case, go ahead.


Undocumented API notes for anyone wanting to improve this or make their own:

All API requests expect `"token": "<server token>"` in the header. Failure to provide a valid token will result in an error.

https://api.craftingstore.net/v4/queue/markComplete
Expects:
```
{
  "removeIds": "[ID1, ID2, ID3, ID4, ...]"
}
```

Returns:
```
{
  "success": true,
  "error": null,
  "message": null
}
```

https://api.craftingstore.net/v4/queue
Expects:
```
Nothing
```

Returns:
```
{
  "success": true,
  "error": null,
  "message": null,
  "result": [
    {
      "id": 12345678,
      "paymentId": 23456789,
      "command": "broadcast Hello, World!",
      "mcName": "Notch",
      "uuid": "1234567890abcdef1234567890abcdef1234",
      "packageName": "Hello",
      "packagePrice": 10,
      "packagePriceCents": 1000,
      "couponDiscount": 0,
      "couponName": null,
      "requireOnline": false
    },
    {
      "id": 12345679,
      "paymentId": 23456789,
      "command": "broadcast Goodbye, World!",
      "mcName": "Notch",
      "uuid": "1234567890abcdef1234567890abcdef1234",
      "packageName": "Hello",
      "packagePrice": 5,
      "packagePriceCents": 500,
      "couponDiscount": 0,
      "couponName": null,
      "requireOnline": false
    }
  ]
}
```

https://api.craftingstore.net/v7/payments
Documentation can be found here: https://docs.api.craftingstore.net/#/payments/overview
    
