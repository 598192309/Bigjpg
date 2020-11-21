//
//  KKUDID.m
//
//  Created by Jaykon on 13-11-14.
//  Copyright (c) 2013年 jcoder. All rights reserved.
//

#import "KKUUID.h"
#import <AdSupport/AdSupport.h>
#import <SAMKeychain.h>
#include <sys/sysctl.h>
#include <net/if.h>
#include <net/if_dl.h>


#define KeyChainService  [NSBundle mainBundle].bundleIdentifier
#define KeyChainAccount  @"BigJpg"

@implementation KKUUID
+ (NSString *)getUUID
{
    static NSString *uuid;
    if (uuid) {
        return uuid;
    }
    uuid = [SAMKeychain passwordForService:KeyChainService account:KeyChainAccount];
    if (!uuid) {
        uuid = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
        if (!uuid) {
            return @"";
        }
        [SAMKeychain setPassword:uuid forService:KeyChainService account:KeyChainAccount];
        NSLog(@"产生一个新的UUID：%@",uuid);
    } else {
        NSLog(@"使用以往的的UUID：%@",uuid);
    }
    
    NSLog(@"%@",uuid);
    return uuid;
}

+ (NSString *)getMacAdress
{
    int mib[6];
    size_t len;
    char *buf;
    unsigned char *ptr;
    struct if_msghdr *ifm;
    struct sockaddr_dl *sdl;
    
    mib[0] = CTL_NET;
    mib[1] = AF_ROUTE;
    mib[2] = 0;
    mib[3] = AF_LINK;
    mib[4] = NET_RT_IFLIST;
    
    if ((mib[5] = if_nametoindex("en0")) == 0) {
        printf("Error: if_nametoindex error\n");
        return NULL;
    }
    
    if (sysctl(mib, 6, NULL, &len, NULL, 0) < 0) {
        printf("Error: sysctl, take 1\n");
        return NULL;
    }
    
    if ((buf = malloc(len)) == NULL) {
        printf("Could not allocate memory. error!\n");
        return NULL;
    }
    
    if (sysctl(mib, 6, buf, &len, NULL, 0) < 0) {
        printf("Error: sysctl, take 2");
        free(buf);
        return NULL;
    }
    
    ifm = (struct if_msghdr *)buf;
    sdl = (struct sockaddr_dl *)(ifm + 1);
    ptr = (unsigned char *)LLADDR(sdl);
    NSString *macString = [NSString stringWithFormat:@"%02X:%02X:%02X:%02X:%02X:%02X",
                           *ptr, *(ptr+1), *(ptr+2), *(ptr+3), *(ptr+4), *(ptr+5)];
    free(buf);
    
    return macString;


}

+ (NSString *)getIDFA
{
//    if([[[UIDevice currentDevice] systemVersion] floatValue]>=6.0){
//        if([ASIdentifierManager sharedManager].advertisingTrackingEnabled){
//            return [[ASIdentifierManager sharedManager].advertisingIdentifier UUIDString];
//        }
//        return nil;
//    }
//    return nil;
    
    NSBundle *adSupportBundle = [NSBundle bundleWithPath:@"/System/Library/Frameworks/AdSupport.framework"];
    [adSupportBundle load];
    
    if (adSupportBundle == nil) {
        return @"";
    }
    else{
        
        Class asIdentifierMClass = NSClassFromString(@"ASIdentifierManager");
        
        if(asIdentifierMClass == nil){
            return @"";
        }
        else{
            
            //for no arc
            //ASIdentifierManager *asIM = [[[asIdentifierMClass alloc] init] autorelease];
            //for arc
            ASIdentifierManager *asIM = [[asIdentifierMClass alloc] init];
            
            if (asIM == nil) {
                return @"";
            }
            else{
                
                if(asIM.advertisingTrackingEnabled){
                    return [asIM.advertisingIdentifier UUIDString];
                }
                else{
                    return [asIM.advertisingIdentifier UUIDString];
                }
            }
        }
    }
}

+ (NSString *)getOSVsersion {
    return [[UIDevice currentDevice] systemVersion];
}


+ (NSString *)getDeviceModel {
    size_t size;
    sysctlbyname("hw.machine",NULL, &size,NULL, 0);
    char*machine = (char*)malloc(size);
    sysctlbyname("hw.machine", machine, &size,NULL, 0);
    NSString*platform = [NSString stringWithCString:machine encoding:NSUTF8StringEncoding];
    free(machine);
    return platform;
}
@end
